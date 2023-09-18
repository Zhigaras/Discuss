package com.zhigaras.calls.webrtc

import android.content.Context
import com.google.gson.Gson
import com.zhigaras.calls.data.CallsCloudService
import com.zhigaras.calls.domain.model.ConnectionData
import com.zhigaras.calls.domain.model.ConnectionDataType
import com.zhigaras.cloudeservice.CloudServiceImpl
import com.zhigaras.cloudeservice.ProvideDatabase
import org.webrtc.AudioTrack
import org.webrtc.Camera2Enumerator
import org.webrtc.CameraVideoCapturer
import org.webrtc.EglBase
import org.webrtc.IceCandidate
import org.webrtc.MediaConstraints
import org.webrtc.PeerConnection
import org.webrtc.PeerConnection.IceServer
import org.webrtc.SessionDescription
import org.webrtc.SurfaceTextureHelper
import org.webrtc.SurfaceViewRenderer
import org.webrtc.VideoTrack

class WebRtcClient(
    private val application: Context,
    observer: PeerConnection.Observer,
    private val gson: Gson = Gson(),
    private val callsCloudService: CallsCloudService = CallsCloudService.Base(
        CloudServiceImpl(ProvideDatabase.Base())
    )
) {
    private val eglBaseContext = EglBase.create().eglBaseContext
    private val peerConnectionFactory = MyPeerConnectionFactory(eglBaseContext)
    private val mediaConstraints = MediaConstraints().also {
        it.mandatory.add(MediaConstraints.KeyValuePair("OfferToReceiveVideo", "true"))
    }
    
    init {
        peerConnectionFactory.init(application)
    }
    
    private val iceServers = arrayListOf(
        IceServer.builder("turn:a.relay.metered.ca:443?transport=tcp")
            .setUsername("83eebabf8b4cce9d5dbcb649")
            .setPassword("2D7JvfkOQtBdYW3R").createIceServer()
    )
    private val peerConnection = peerConnectionFactory.createPeerConnection(iceServers, observer)
    private val localVideoSource = peerConnectionFactory.createVideoSource(false)
    private val localAudioSource = peerConnectionFactory.createAudioSource(MediaConstraints())
    private val videoCapturer = getVideoCapturer()
    private lateinit var localVideoTrack: VideoTrack
    private lateinit var localAudioTrack: AudioTrack
    
    fun initLocalSurfaceView(view: SurfaceViewRenderer) {
        initSurfaceViewRenderer(view)
        startLocalVideoStreaming(view)
    }
    
    private fun initSurfaceViewRenderer(viewRenderer: SurfaceViewRenderer) {
        viewRenderer.setEnableHardwareScaler(true)
        viewRenderer.setMirror(true)
        viewRenderer.init(eglBaseContext, null)
    }
    
    private fun startLocalVideoStreaming(view: SurfaceViewRenderer) {
        val helper = SurfaceTextureHelper.create(Thread.currentThread().name, eglBaseContext)
        videoCapturer.initialize(helper, application, localVideoSource.capturerObserver)
        videoCapturer.startCapture(480, 360, 30)
        localVideoTrack = peerConnectionFactory.createVideoTrack(localVideoSource)
            .apply { addSink(view) }
        localAudioTrack = peerConnectionFactory.createAudioTrack(localAudioSource)
        val localStream = peerConnectionFactory.createLocalMediaStream().apply {
            addTrack(localVideoTrack)
            addTrack(localAudioTrack)
        }
        peerConnection!!.addStream(localStream)
    }
    
    private fun getVideoCapturer(): CameraVideoCapturer {
        val enumerator = Camera2Enumerator(application)
        val deviceNames = enumerator.deviceNames
        for (device in deviceNames) {
            if (enumerator.isFrontFacing(device)) {
                return enumerator.createCapturer(device, null)
            }
        }
        throw IllegalStateException("front facing camera not found")
    }
    
    fun initRemoteSurfaceView(view: SurfaceViewRenderer) {
        initSurfaceViewRenderer(view)
    }
    
    @Throws(NullPointerException::class)
    fun call(target: String, userId: String) {
        if (peerConnection == null) throw NullPointerException()
        val data: (SessionDescription) -> ConnectionData = {
            ConnectionData(target, userId, it.description, ConnectionDataType.OFFER)
        }
        peerConnection.createOffer(object : SimpleSdpObserver() {
            override fun onCreateSuccess(sessionDescription: SessionDescription) {
                peerConnection.setLocalDescription(object : SimpleSdpObserver() {
                    override fun onSetSuccess() {
                        callsCloudService.sendToCloud(data(sessionDescription))
                    }
                }, sessionDescription)
            }
        }, mediaConstraints)
    }
    
    @Throws(NullPointerException::class)
    fun answer(target: String, userId: String) {
        if (peerConnection == null) throw NullPointerException()
        val data: (SessionDescription) -> ConnectionData = {
            ConnectionData(target, userId, it.description, ConnectionDataType.ANSWER)
        }
        peerConnection.createAnswer(object : SimpleSdpObserver() {
            override fun onCreateSuccess(sessionDescription: SessionDescription) {
                peerConnection.setLocalDescription(object : SimpleSdpObserver() {
                    override fun onSetSuccess() {
                        callsCloudService.sendToCloud(data(sessionDescription))
                    }
                }, sessionDescription)
            }
        }, mediaConstraints)
    }
    
    fun onRemoteSessionReceived(sessionDescription: SessionDescription?) {
        peerConnection!!.setRemoteDescription(SimpleSdpObserver(), sessionDescription)
    }
    
    fun addIceCandidate(iceCandidate: IceCandidate?) {
        peerConnection!!.addIceCandidate(iceCandidate)
    }
    
    fun sendIceCandidate(iceCandidate: IceCandidate?, target: String, userId: String) {
        addIceCandidate(iceCandidate)
        callsCloudService.sendToCloud(
            ConnectionData(
                target, userId, gson.toJson(iceCandidate), ConnectionDataType.ICE_CANDIDATE
            )
        )
    }
    
    fun switchCamera() {
        videoCapturer.switchCamera(null)
    }
    
    fun closeConnection() {
        try {
            localVideoTrack.dispose()
            videoCapturer.stopCapture()
            videoCapturer.dispose()
            peerConnection!!.close()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}
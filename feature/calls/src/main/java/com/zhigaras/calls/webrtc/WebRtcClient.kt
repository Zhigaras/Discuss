package com.zhigaras.calls.webrtc

import android.content.Context
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
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
    private val pendingIceMutex = Mutex()
    private val pendingIceCandidates = mutableListOf<IceCandidate>()
    private lateinit var localVideoTrack: VideoTrack
    private lateinit var localAudioTrack: AudioTrack
    
    fun initLocalSurfaceView(view: SurfaceViewRenderer) {
        initSurfaceViewRenderer(view)
        startLocalVideoStreaming(view)
    }
    
    fun provideConnectionState() = peerConnection?.connectionState()
    
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
    
    suspend fun reconnect(): SessionDescription {
        if (peerConnection == null) throw NullPointerException()
        return suspendCreateSessionDescription {
            peerConnection.createOffer(it, MediaConstraints().apply {
                mandatory.add(MediaConstraints.KeyValuePair("OfferToReceiveVideo", "true"))
                mandatory.add(MediaConstraints.KeyValuePair("IceRestart", "true"))
            })
        }
    }
    
    suspend fun createOffer(): SessionDescription {
        if (peerConnection == null) throw NullPointerException()
        return suspendCreateSessionDescription { peerConnection.createOffer(it, mediaConstraints) }
    }
    
    suspend fun createAnswer(): SessionDescription {
        if (peerConnection == null) throw NullPointerException()
        return suspendCreateSessionDescription { peerConnection.createAnswer(it, mediaConstraints) }
    }
    
    suspend fun setRemoteDescription(sessionDescription: SessionDescription) {
        if (peerConnection == null) throw NullPointerException()
        return suspendSdpObserver {
            peerConnection.setRemoteDescription(it, sessionDescription)
        }.also {
            pendingIceMutex.withLock {
                pendingIceCandidates.forEach { iceCandidate ->
                    peerConnection.addRtcIceCandidate(iceCandidate)
                }
                pendingIceCandidates.clear()
            }
        }
    }
    
    suspend fun setLocalDescription(sessionDescription: SessionDescription) {
        if (peerConnection == null) throw NullPointerException()
        return suspendSdpObserver { peerConnection.setLocalDescription(it, sessionDescription) }
    }
    
    suspend fun addIceCandidate(iceCandidate: IceCandidate) {
        if (peerConnection?.remoteDescription == null) {
            pendingIceMutex.withLock {
                pendingIceCandidates.add(iceCandidate)
            }
            return
        }
        peerConnection.addRtcIceCandidate(iceCandidate)
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
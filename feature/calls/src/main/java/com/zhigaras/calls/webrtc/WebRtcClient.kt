package com.zhigaras.calls.webrtc

import android.content.Context
import com.google.gson.Gson
import org.webrtc.AudioSource
import org.webrtc.AudioTrack
import org.webrtc.Camera2Enumerator
import org.webrtc.CameraVideoCapturer
import org.webrtc.DefaultVideoDecoderFactory
import org.webrtc.DefaultVideoEncoderFactory
import org.webrtc.EglBase
import org.webrtc.IceCandidate
import org.webrtc.MediaConstraints
import org.webrtc.MediaStream
import org.webrtc.PeerConnection
import org.webrtc.PeerConnection.IceServer
import org.webrtc.PeerConnectionFactory
import org.webrtc.PeerConnectionFactory.InitializationOptions
import org.webrtc.SessionDescription
import org.webrtc.SurfaceTextureHelper
import org.webrtc.SurfaceViewRenderer
import org.webrtc.VideoSource
import org.webrtc.VideoTrack

import com.zhigaras.calls.domain.model.DataModel
import com.zhigaras.calls.domain.model.DataModelType

class WebRTCClient(
    private val context: Context,
    observer: PeerConnection.Observer,
    private val username: String,
    private val gson: Gson = Gson()
) {
    private val eglBaseContext = EglBase.create().eglBaseContext
    private val peerConnectionFactory: PeerConnectionFactory
    private val peerConnection: PeerConnection?
    private val iceServer: MutableList<IceServer> = ArrayList()
    private var videoCapturer: CameraVideoCapturer? = null
    private val localVideoSource: VideoSource
    private val localAudioSource: AudioSource
    private val localTrackId = "local_track"
    private val localStreamId = "local_stream"
    private lateinit var localVideoTrack: VideoTrack
    private lateinit var localAudioTrack: AudioTrack
    private lateinit var localStream: MediaStream
    private val mediaConstraints = MediaConstraints()
    lateinit var listener: Listener
    
    init {
        initPeerConnectionFactory()
        peerConnectionFactory = createPeerConnectionFactory()
        iceServer.add(
            IceServer.builder("turn:a.relay.metered.ca:443?transport=tcp")
                .setUsername("83eebabf8b4cce9d5dbcb649")
                .setPassword("2D7JvfkOQtBdYW3R").createIceServer()
        )
        peerConnection = createPeerConnection(observer)
        localVideoSource = peerConnectionFactory.createVideoSource(false)
        localAudioSource = peerConnectionFactory.createAudioSource(MediaConstraints())
        mediaConstraints.mandatory.add(MediaConstraints.KeyValuePair("OfferToReceiveVideo", "true"))
    }
    
    //initializing peer connection section
    private fun initPeerConnectionFactory() {
        val options = InitializationOptions.builder(
            context
        ).setFieldTrials("WebRTC-H264HighProfile/Enabled/").setEnableInternalTracer(true)
            .createInitializationOptions()
        PeerConnectionFactory.initialize(options)
    }
    
    private fun createPeerConnectionFactory(): PeerConnectionFactory {
        val options = PeerConnectionFactory.Options()
        options.disableEncryption = false
        options.disableNetworkMonitor = false
        return PeerConnectionFactory.builder()
            .setVideoEncoderFactory(DefaultVideoEncoderFactory(eglBaseContext, true, true))
            .setVideoDecoderFactory(DefaultVideoDecoderFactory(eglBaseContext))
            .setOptions(options).createPeerConnectionFactory()
    }
    
    private fun createPeerConnection(observer: PeerConnection.Observer): PeerConnection? {
        return peerConnectionFactory.createPeerConnection(iceServer, observer)
    }
    
    //initilizing ui like surface view renderers
    fun initSurfaceViewRendere(viewRenderer: SurfaceViewRenderer) {
        viewRenderer.setEnableHardwareScaler(true)
        viewRenderer.setMirror(true)
        viewRenderer.init(eglBaseContext, null)
    }
    
    fun initLocalSurfaceView(view: SurfaceViewRenderer) {
        initSurfaceViewRendere(view)
        startLocalVideoStreaming(view)
    }
    
    private fun startLocalVideoStreaming(view: SurfaceViewRenderer) {
        val helper = SurfaceTextureHelper.create(
            Thread.currentThread().name, eglBaseContext
        )
        videoCapturer = getVideoCapturer()
        videoCapturer!!.initialize(helper, context, localVideoSource.capturerObserver)
        videoCapturer!!.startCapture(480, 360, 15)
        localVideoTrack = peerConnectionFactory.createVideoTrack(
            localTrackId + "_video", localVideoSource
        )
        localVideoTrack.addSink(view)
        localAudioTrack =
            peerConnectionFactory.createAudioTrack(localTrackId + "_audio", localAudioSource)
        localStream = peerConnectionFactory.createLocalMediaStream(localStreamId)
        localStream.addTrack(localVideoTrack)
        localStream.addTrack(localAudioTrack)
        peerConnection!!.addStream(localStream)
    }
    
    private fun getVideoCapturer(): CameraVideoCapturer {
        val enumerator = Camera2Enumerator(context)
        val deviceNames = enumerator.deviceNames
        for (device in deviceNames) {
            if (enumerator.isFrontFacing(device)) {
                return enumerator.createCapturer(device, null)
            }
        }
        throw IllegalStateException("front facing camera not found")
    }
    
    fun initRemoteSurfaceView(view: SurfaceViewRenderer) {
        initSurfaceViewRendere(view)
    }
    
    //negotiation section like call and answer
    fun call(target: String) {
        try {
            peerConnection!!.createOffer(object : SimpleSdpObserver() {
                override fun onCreateSuccess(sessionDescription: SessionDescription) {
                    super.onCreateSuccess(sessionDescription)
                    peerConnection.setLocalDescription(object : SimpleSdpObserver() {
                        override fun onSetSuccess() {
                            super.onSetSuccess()
                            //its time to transfer this sdp to other peer
                            if (listener != null) {
                                listener.onTransferDataToOtherPeer(
                                    DataModel(
                                        target,
                                        username,
                                        sessionDescription.description,
                                        DataModelType.OFFER
                                    )
                                )
                            }
                        }
                    }, sessionDescription)
                }
            }, mediaConstraints)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
    
    fun answer(target: String) {
        try {
            peerConnection!!.createAnswer(object : SimpleSdpObserver() {
                override fun onCreateSuccess(sessionDescription: SessionDescription) {
                    super.onCreateSuccess(sessionDescription)
                    peerConnection.setLocalDescription(object : SimpleSdpObserver() {
                        override fun onSetSuccess() {
                            super.onSetSuccess()
                            //its time to transfer this sdp to other peer
                            if (listener != null) {
                                listener.onTransferDataToOtherPeer(
                                    DataModel(
                                        target,
                                        username,
                                        sessionDescription.description,
                                        DataModelType.ANSWER
                                    )
                                )
                            }
                        }
                    }, sessionDescription)
                }
            }, mediaConstraints)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
    
    fun onRemoteSessionReceived(sessionDescription: SessionDescription?) {
        peerConnection!!.setRemoteDescription(SimpleSdpObserver(), sessionDescription)
    }
    
    fun addIceCandidate(iceCandidate: IceCandidate?) {
        peerConnection!!.addIceCandidate(iceCandidate)
    }
    
    fun sendIceCandidate(iceCandidate: IceCandidate?, target: String) {
        addIceCandidate(iceCandidate)
        if (listener != null) {
            listener.onTransferDataToOtherPeer(
                DataModel(
                    target, username, gson.toJson(iceCandidate), DataModelType.ICE_CANDIDATE
                )
            )
        }
    }
    
    fun switchCamera() {
        videoCapturer!!.switchCamera(null)
    }
    
    fun toggleVideo(shouldBeMuted: Boolean?) {
        localVideoTrack!!.setEnabled(shouldBeMuted!!)
    }
    
    fun toggleAudio(shouldBeMuted: Boolean?) {
        localAudioTrack!!.setEnabled(shouldBeMuted!!)
    }
    
    fun closeConnection() {
        try {
            localVideoTrack!!.dispose()
            videoCapturer!!.stopCapture()
            videoCapturer!!.dispose()
            peerConnection!!.close()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
    
    interface Listener {
        fun onTransferDataToOtherPeer(model: DataModel)
    }
}
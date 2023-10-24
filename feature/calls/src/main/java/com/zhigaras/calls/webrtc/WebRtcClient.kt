package com.zhigaras.calls.webrtc

import androidx.lifecycle.Observer
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import org.webrtc.AudioTrack
import org.webrtc.Camera2Enumerator
import org.webrtc.CameraVideoCapturer
import org.webrtc.DataChannel
import org.webrtc.EglBase
import org.webrtc.IceCandidate
import org.webrtc.MediaConstraints
import org.webrtc.PeerConnection
import org.webrtc.SessionDescription
import org.webrtc.SurfaceTextureHelper
import org.webrtc.SurfaceViewRenderer
import org.webrtc.VideoTrack
import java.nio.ByteBuffer

class WebRtcClient(
    private val iceServers: IceServersList,
    private val peerConnectionObserver: MyPeerConnectionObserver,
    private val eglBaseContext: EglBase.Context,
    private val peerConnectionFactory: MyPeerConnectionFactory,
    private val enumerator: Camera2Enumerator,
) : PeerConnectionCommunication.ObserveForever {
    private var peerConnection: PeerConnection? = null
    private val localVideoSource = peerConnectionFactory.createVideoSource(false)
    private val localAudioSource = peerConnectionFactory.createAudioSource(MediaConstraints())
    private lateinit var videoCapturer: CameraVideoCapturer
    private val pendingIceMutex = Mutex()
    private val pendingIceCandidates = mutableListOf<IceCandidate>()
    private var dataChannel: DataChannel? = null
    private lateinit var localVideoTrack: VideoTrack
    private lateinit var localAudioTrack: AudioTrack
    
    init {
        initNewConnection()
    }
    
    private fun initNewConnection() {
        peerConnection = peerConnectionFactory.createPeerConnection(
            iceServers.provide(),
            peerConnectionObserver.provideObserver()
        )
        dataChannel = peerConnection?.createDataChannel(
            "messaging",
            DataChannel.Init()
        )
    }
    
    override fun observeForever(observer: Observer<PeerConnectionState>) {
        peerConnectionObserver.observeForever(observer)
    }
    
    override fun removeObserver(observer: Observer<PeerConnectionState>) {
        peerConnectionObserver.removeObserver(observer)
    }
    
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
        videoCapturer = getVideoCapturer()
        val helper = SurfaceTextureHelper.create(Thread.currentThread().name, eglBaseContext)
        videoCapturer.initialize(helper, view.context, localVideoSource.capturerObserver)
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
    
    suspend fun createOffer(mediaConstraints: MediaConstraints): SessionDescription {
        return suspendCreateSessionDescription { peerConnection?.createOffer(it, mediaConstraints) }
    }
    
    suspend fun createAnswer(mediaConstraints: MediaConstraints): SessionDescription {
        return suspendCreateSessionDescription {
            peerConnection?.createAnswer(it, mediaConstraints)
        }
    }
    
    suspend fun setRemoteDescription(sessionDescription: SessionDescription) {
        return suspendSdpObserver {
            peerConnection?.setRemoteDescription(it, sessionDescription)
        }.also {
            pendingIceMutex.withLock {
                pendingIceCandidates.forEach { iceCandidate ->
                    peerConnection?.addRtcIceCandidate(iceCandidate)
                }
                pendingIceCandidates.clear()
            }
        }
    }
    
    suspend fun setLocalDescription(sessionDescription: SessionDescription) {
        return suspendSdpObserver { peerConnection?.setLocalDescription(it, sessionDescription) }
    }
    
    suspend fun addIceCandidate(iceCandidate: IceCandidate) {
        if (peerConnection?.remoteDescription == null) {
            pendingIceMutex.withLock {
                pendingIceCandidates.add(iceCandidate)
            }
            return
        }
        peerConnection?.addRtcIceCandidate(iceCandidate)
    }
    
    fun sendMessage(text: String) {
        val buffer = ByteBuffer.wrap(text.toByteArray())
        dataChannel?.send(DataChannel.Buffer(buffer, false))
    }
    
    fun closeCurrentAndCreateNewConnection() {
        peerConnection?.close()
        dataChannel?.close()
        initNewConnection()
    }
    
    fun closeConnectionTotally() {
        try {
            localVideoTrack.dispose()
            videoCapturer.stopCapture()
            videoCapturer.dispose()
            peerConnection!!.close()
            dataChannel?.close()
            peerConnectionObserver.closeConnection()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}
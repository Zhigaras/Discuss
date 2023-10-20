package com.zhigaras.calls.webrtc

import androidx.lifecycle.Observer
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
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
    iceServers: IceServersList,
    private val observer: MyPeerConnectionObserver,
    private val eglBaseContext: EglBase.Context,
    private val peerConnectionFactory: MyPeerConnectionFactory,
    private val enumerator: Camera2Enumerator,
) {
    private val peerConnection: PeerConnection? = peerConnectionFactory.createPeerConnection(
        iceServers.provide(),
        observer.provideObserver()
    )
    private val localVideoSource = peerConnectionFactory.createVideoSource(false)
    private val localAudioSource = peerConnectionFactory.createAudioSource(MediaConstraints())
    private val videoCapturer = getVideoCapturer()
    private val pendingIceMutex = Mutex()
    private val pendingIceCandidates = mutableListOf<IceCandidate>()
    private val dataChannel = peerConnection!!.createDataChannel("messaging", DataChannel.Init()) // TODO: fix no null assertion
    private lateinit var localVideoTrack: VideoTrack
    private lateinit var localAudioTrack: AudioTrack
    
    fun observeForever(scope: CoroutineScope, observer: Observer<PeerConnectionState>) {
        scope.launch {
            this@WebRtcClient.observer.observeForever(observer)
        }
    }
    
//    override fun removeObserver(observer: Observer<PeerConnectionState>) {
//        this.observer.removeObserver(observer)
//    }
    
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
        if (peerConnection == null) throw NullPointerException()
        return suspendCreateSessionDescription { peerConnection.createOffer(it, mediaConstraints) }
    }
    
    suspend fun createAnswer(mediaConstraints: MediaConstraints): SessionDescription {
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
    
    fun sendMessage(text: String) {
        val buffer = ByteBuffer.wrap(text.toByteArray())
        dataChannel.send(DataChannel.Buffer(buffer, false))
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
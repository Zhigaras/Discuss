package com.zhigaras.calls.webrtc

import android.app.Application
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
import org.webrtc.MediaStream
import org.webrtc.PeerConnection
import org.webrtc.SessionDescription
import org.webrtc.SurfaceTextureHelper
import org.webrtc.SurfaceViewRenderer
import org.webrtc.VideoTrack
import java.nio.ByteBuffer

class WebRtcClient(
    private val application: Application,
    private val iceServers: IceServersList,
    private val peerConnectionObserver: PeerConnectionObserveWrapper,
    private val eglBaseContext: EglBase.Context,
    private val peerConnectionFactory: MyPeerConnectionFactory,
    private val enumerator: Camera2Enumerator,
) {
    private var peerConnection: PeerConnection? = null
    private val localVideoSource = peerConnectionFactory.createVideoSource(false)
    private val localAudioSource = peerConnectionFactory.createAudioSource(MediaConstraints())
    private val pendingIceMutex = Mutex()
    private val pendingIceCandidates = mutableListOf<IceCandidate>()
    private var dataChannel: DataChannel? = null
    private lateinit var videoCapturer: CameraVideoCapturer
    private var localVideoTrack: VideoTrack? = null
    private var localAudioTrack: AudioTrack? = null
    private var localStream: MediaStream = peerConnectionFactory.createLocalMediaStream()
    private var localView: SurfaceViewRenderer? = null
    
    fun initNewConnection(observer: Observer<PeerConnectionState>) {
        peerConnection = peerConnectionFactory.createPeerConnection(
            iceServers.provide(),
            peerConnectionObserver.provideObserver()
        )
        dataChannel = peerConnection?.createDataChannel("messaging", DataChannel.Init())
        peerConnectionObserver.observeForever(observer)
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
        localVideoTrack?.removeSink(localView)
        localView?.release()
        localView = view
        videoCapturer = getVideoCapturer()
        val helper = SurfaceTextureHelper.create(Thread.currentThread().name, eglBaseContext)
        videoCapturer.initialize(helper, application, localVideoSource.capturerObserver)
        videoCapturer.startCapture(480, 360, 30)
        localAudioTrack?.let { localStream.removeTrack(it) }
        localVideoTrack?.let { localStream.removeTrack(it) }
        localVideoTrack =
            peerConnectionFactory.createVideoTrack(localVideoSource).apply { addSink(localView) }
        localAudioTrack = peerConnectionFactory.createAudioTrack(localAudioSource)
        localAudioTrack?.let { localStream.addTrack(it) }
        localVideoTrack?.let { localStream.addTrack(it) }
        addLocalStream()
    }
    
    fun addLocalStream() {
        peerConnection?.addStream(localStream)
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
    
    fun closeCurrentConnection(observer: Observer<PeerConnectionState>) {
        peerConnection?.close()
        dataChannel?.unregisterObserver()
        dataChannel?.close()
        pendingIceCandidates.clear()
//        peerConnectionObserver.removeObserver(observer)
    }
    
    fun closeConnectionTotally(observer: Observer<PeerConnectionState>) {
        closeCurrentConnection(observer)
        localVideoTrack?.removeSink(localView)
        localView?.release()
        localView = null
        videoCapturer.dispose()
        localVideoSource.dispose()
        localAudioSource.dispose()
        localStream.dispose()
        peerConnectionObserver.closeConnection()
    }
}
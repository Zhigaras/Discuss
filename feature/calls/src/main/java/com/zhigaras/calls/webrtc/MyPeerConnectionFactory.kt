package com.zhigaras.calls.webrtc

import android.content.Context
import org.webrtc.AudioSource
import org.webrtc.AudioTrack
import org.webrtc.DefaultVideoDecoderFactory
import org.webrtc.DefaultVideoEncoderFactory
import org.webrtc.EglBase
import org.webrtc.MediaConstraints
import org.webrtc.MediaStream
import org.webrtc.PeerConnection
import org.webrtc.PeerConnection.IceServer
import org.webrtc.PeerConnectionFactory
import org.webrtc.VideoSource
import org.webrtc.VideoTrack

class MyPeerConnectionFactory(
    private val eglBaseContext: EglBase.Context,
    private val options: PeerConnectionFactory.Options = PeerConnectionFactory.Options()
        .apply {
            disableEncryption = false
            disableNetworkMonitor = false
        }
) {
    private val localTrackId = "local_track"
    private val localStreamId = "local_stream"
    
    private lateinit var factory: PeerConnectionFactory
    
    fun init(appContext: Context) {
        val initOptions = PeerConnectionFactory.InitializationOptions.builder(appContext)
            .setFieldTrials("WebRTC-H264HighProfile/Enabled/")
            .setEnableInternalTracer(true)
            .createInitializationOptions()
        PeerConnectionFactory.initialize(initOptions)
        
        factory = PeerConnectionFactory.builder()
            .setVideoEncoderFactory(DefaultVideoEncoderFactory(eglBaseContext, true, true))
            .setVideoDecoderFactory(DefaultVideoDecoderFactory(eglBaseContext))
            .setOptions(options).createPeerConnectionFactory()
    }
    
    fun createVideoSource(isScreencast: Boolean): VideoSource {
        return factory.createVideoSource(isScreencast)
    }
    
    fun createAudioSource(constraints: MediaConstraints): AudioSource {
        return factory.createAudioSource(constraints)
    }
    
    fun createPeerConnection(
        iceServers: List<IceServer>,
        observer: PeerConnection.Observer
    ): PeerConnection? {
        return factory.createPeerConnection(iceServers, observer)
    }
    
    fun createVideoTrack(source: VideoSource): VideoTrack {
        return factory.createVideoTrack("${localTrackId}_video", source)
    }
    
    fun createAudioTrack(source: AudioSource): AudioTrack {
        return factory.createAudioTrack("${localTrackId}_audio", source)
    }
    
    fun createLocalMediaStream(): MediaStream {
        return factory.createLocalMediaStream(localStreamId)
    }
}
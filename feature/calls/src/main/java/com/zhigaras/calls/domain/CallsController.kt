package com.zhigaras.calls.domain

import android.content.Context
import com.zhigaras.auth.ProvideUserId
import com.zhigaras.calls.domain.model.ConnectionData
import com.zhigaras.calls.webrtc.PeerConnectionCallback
import com.zhigaras.calls.webrtc.SimplePeerConnectionObserver
import com.zhigaras.calls.webrtc.WebRtcClient
import com.zhigaras.cloudeservice.CloudService
import org.webrtc.IceCandidate
import org.webrtc.MediaStream
import org.webrtc.PeerConnection.PeerConnectionState
import org.webrtc.SurfaceViewRenderer

interface CallsController {
    
    fun startNegotiation(opponentId: String, userId: String)
    
    fun subscribeToConnectionEvents(userId: String)
    
    fun initLocalView(view: SurfaceViewRenderer)
    
    fun initRemoteView(view: SurfaceViewRenderer)
    
    fun initConnectionCallback(callback: PeerConnectionCallback)
    
    fun setOpponentId(opponentId: String)
    
    class Base(
        application: Context,
        private val callsCloudService: CallsCloudService,
        provideUserId: ProvideUserId
    ) : CallsController {
        private var remoteView: SurfaceViewRenderer? = null
        private val userId = provideUserId.provide()
        private lateinit var webRtcClient: WebRtcClient
        private lateinit var target: String
        private lateinit var peerConnectionCallback: PeerConnectionCallback
        
        init {
            webRtcClient =
                WebRtcClient(application, callsCloudService, object : SimplePeerConnectionObserver {
                    override fun onAddStream(mediaStream: MediaStream) {
                        super.onAddStream(mediaStream)
                        try {
                            mediaStream.videoTracks[0].addSink(remoteView)
                        } catch (e: Exception) {
                            throw Exception("Can`t add stream")
                        }
                    }
                    
                    override fun onConnectionChange(newState: PeerConnectionState) {
                        super.onConnectionChange(newState)
                        peerConnectionCallback.invoke(newState)
                        if (newState == PeerConnectionState.CONNECTED) {
                            callsCloudService.removeConnectionData(userId)
                        }
                        // TODO: handle reconnecting while internet connection down
                    }
                    
                    override fun onIceCandidate(iceCandidate: IceCandidate) {
                        super.onIceCandidate(iceCandidate)
                        webRtcClient.sendIceCandidate(iceCandidate, target, userId)
                    }
                })
        }
        
        override fun initLocalView(view: SurfaceViewRenderer) {
            webRtcClient.initLocalSurfaceView(view)
        }
        
        override fun initRemoteView(view: SurfaceViewRenderer) {
            webRtcClient.initRemoteSurfaceView(view)
            remoteView = view
        }
        
        override fun initConnectionCallback(callback: PeerConnectionCallback) {
            peerConnectionCallback = callback
        }
        
        fun switchCamera() {
            webRtcClient.switchCamera()
        }
        
        override fun startNegotiation(opponentId: String, userId: String) {
            subscribeToConnectionEvents(userId)
            webRtcClient.call(opponentId, userId)
        }
        
        override fun setOpponentId(opponentId: String) {
            target = opponentId
        }
        
        fun endCall() {
            webRtcClient.closeConnection()
        }
        
        override fun subscribeToConnectionEvents(userId: String) {
            callsCloudService.observeUpdates(
                userId,
                object : CloudService.Callback<ConnectionData> {
                    override fun provide(data: ConnectionData) {
                        target = data.sender
                        data.handle(webRtcClient)
                    }
                    
                    override fun error(message: String) {
                        throw Exception(message)
                    }
                })
        }
    }
}
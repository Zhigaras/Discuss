package com.zhigaras.calls.domain

import android.content.Context
import com.zhigaras.auth.ProvideUserId
import com.zhigaras.calls.domain.model.ConnectionData
import com.zhigaras.calls.domain.model.MyIceCandidate
import com.zhigaras.calls.domain.model.MySessionDescription
import com.zhigaras.calls.webrtc.PeerConnectionCallback
import com.zhigaras.calls.webrtc.SimplePeerConnectionObserver
import com.zhigaras.calls.webrtc.WebRtcClient
import com.zhigaras.cloudeservice.CloudService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import org.webrtc.IceCandidate
import org.webrtc.MediaStream
import org.webrtc.PeerConnection.PeerConnectionState
import org.webrtc.SessionDescription
import org.webrtc.SurfaceViewRenderer

interface CallsController {
    
    fun sendOffer(opponentId: String, userId: String)
    
    fun sendAnswer(offer: SessionDescription, opponentId: String, userId: String)
    
    fun handleAnswer(answer: SessionDescription)
    
    fun subscribeToConnectionEvents(userId: String)
    
    fun setOpponentId(opponentId: String)
    
    fun handleIceCandidate(iceCandidate: MyIceCandidate)
    
    class Base(
        application: Context,
        private val callsCloudService: CallsCloudService,
        provideUserId: ProvideUserId
    ) : CallsController, InitCalls {
        private var remoteView: SurfaceViewRenderer? = null
        private val userId = provideUserId.provide()
        private val scope = CoroutineScope(SupervisorJob() + Dispatchers.Default)
        private lateinit var target: String
        private lateinit var peerConnectionCallback: PeerConnectionCallback
        private var webRtcClient: WebRtcClient =
            WebRtcClient(application, object : SimplePeerConnectionObserver {
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
//                        if (newState == PeerConnectionState.CONNECTED) {
//                            callsCloudService.removeConnectionData(userId)
//                        }
                    // TODO: handle reconnecting while internet connection down
                }
        
                override fun onIceCandidate(iceCandidate: IceCandidate) {
                    super.onIceCandidate(iceCandidate)
                    callsCloudService.sendToCloud(
                        ConnectionData(
                            target,
                            userId,
                            iceCandidate = MyIceCandidate(iceCandidate)
                        )
                    )
                }
            })
        
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
        
        override fun sendOffer(opponentId: String, userId: String) {
            scope.launch {
                subscribeToConnectionEvents(userId)
                val offer = webRtcClient.createOffer()
                webRtcClient.setLocalDescription(offer)
                callsCloudService.sendToCloud(
                    ConnectionData(opponentId, userId, offer = MySessionDescription(offer))
                )
            }
        }
        
        override fun sendAnswer(offer: SessionDescription, opponentId: String, userId: String) {
            scope.launch {
                webRtcClient.setRemoteDescription(offer)
                val answer = webRtcClient.createAnswer()
                webRtcClient.setLocalDescription(answer)
                callsCloudService.sendToCloud(
                    ConnectionData(opponentId, userId, answer = MySessionDescription(answer))
                )
            }
        }
        
        override fun handleIceCandidate(iceCandidate: MyIceCandidate) {
            scope.launch {
                webRtcClient.addIceCandidate(iceCandidate)
            }
        }
        
        override fun handleAnswer(answer: SessionDescription) {
            scope.launch {
                webRtcClient.setRemoteDescription(answer)
            }
        }
        
        override fun setOpponentId(opponentId: String) {
            target = opponentId
        }
        
        override fun subscribeToConnectionEvents(userId: String) {
            callsCloudService.observeUpdates(
                userId,
                object : CloudService.Callback<ConnectionData> {
                    override fun provide(data: ConnectionData) {
                        target = data.sender
                        data.handle(this@Base)
                    }
                    
                    override fun error(message: String) {
                        throw Exception(message)
                    }
                })
        }
    }
}

interface InitCalls {
    
    fun initLocalView(view: SurfaceViewRenderer)
    
    fun initRemoteView(view: SurfaceViewRenderer)
    
    fun initConnectionCallback(callback: PeerConnectionCallback)
}
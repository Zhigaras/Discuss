package com.zhigaras.calls.domain

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import androidx.core.content.ContextCompat
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import com.zhigaras.auth.ProvideUserId
import com.zhigaras.calls.domain.model.ConnectionData
import com.zhigaras.calls.domain.model.MyIceCandidate
import com.zhigaras.calls.domain.model.MySessionDescription
import com.zhigaras.calls.webrtc.PeerConnectionCallback
import com.zhigaras.calls.webrtc.WebRtcClient
import com.zhigaras.cloudeservice.CloudService
import com.zhigaras.core.IntentAction
import com.zhigaras.messaging.domain.DataChannelCommunication
import com.zhigaras.messaging.domain.Messaging
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import org.webrtc.MediaConstraints
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
        provideUserId: ProvideUserId,
        private val callsCloudService: CallsCloudService,
        private val peerConnectionCallback: PeerConnectionCallback,
        private val communication: DataChannelCommunication.Mutable,
        private val webRtcClient: WebRtcClient
    ) : CallsController, InitCalls, Messaging {
        private var remoteView: SurfaceViewRenderer? = null
        private val userId = provideUserId.provide()
        private val scope = CoroutineScope(SupervisorJob() + Dispatchers.Default)
        private var target: String? = null
        
        init {
            webRtcClient.observeForever {
                it.handle(
                    remoteView,
                    peerConnectionCallback,
                    communication,
                    callsCloudService,
                    target,
                    userId
                )
            }
            val connectionReceiver = object : BroadcastReceiver() {
                override fun onReceive(context: Context?, intent: Intent?) {
                    if (intent?.action == IntentAction.ACTION_NETWORK_STATE) {
                        val networkState = intent.getStringExtra("state")
                        val connState = webRtcClient.provideConnectionState()
                        if (
                            networkState == "online" &&
                            (connState == PeerConnectionState.DISCONNECTED || connState == PeerConnectionState.FAILED)
                        ) {
                            reconnect(target!!, userId)
                        }
                    }
                }
            }
            ContextCompat.registerReceiver(
                application,
                connectionReceiver,
                IntentFilter(IntentAction.ACTION_NETWORK_STATE),
                ContextCompat.RECEIVER_NOT_EXPORTED
            )
        }
        
        override fun initLocalView(view: SurfaceViewRenderer) {
            webRtcClient.initLocalSurfaceView(view)
        }
        
        override fun initRemoteView(view: SurfaceViewRenderer) {
            webRtcClient.initRemoteSurfaceView(view)
            remoteView = view
        }
        
        fun reconnect(opponentId: String, userId: String) {
            scope.launch {
                val mediaConstraints = MediaConstraints().apply {
                    mandatory.add(MediaConstraints.KeyValuePair("OfferToReceiveVideo", "true"))
                    mandatory.add(MediaConstraints.KeyValuePair("IceRestart", "true"))
                }
                val offer = webRtcClient.createOffer(mediaConstraints)
                webRtcClient.setLocalDescription(offer)
                callsCloudService.sendToCloud(
                    ConnectionData(opponentId, userId, offer = MySessionDescription(offer))
                )
            }
        }
        
        override fun sendOffer(opponentId: String, userId: String) {
            scope.launch {
                val mediaConstraints = MediaConstraints().also {
                    it.mandatory.add(MediaConstraints.KeyValuePair("OfferToReceiveVideo", "true"))
                }
                subscribeToConnectionEvents(userId)
                val offer = webRtcClient.createOffer(mediaConstraints)
                webRtcClient.setLocalDescription(offer)
                callsCloudService.sendToCloud(
                    ConnectionData(opponentId, userId, offer = MySessionDescription(offer))
                )
            }
        }
        
        override fun sendAnswer(offer: SessionDescription, opponentId: String, userId: String) {
            scope.launch {
                val mediaConstraints = MediaConstraints().also {
                    it.mandatory.add(MediaConstraints.KeyValuePair("OfferToReceiveVideo", "true"))
                }
                webRtcClient.setRemoteDescription(offer)
                val answer = webRtcClient.createAnswer(mediaConstraints)
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
        
        override fun sendMessage(text: String) {
            webRtcClient.sendMessage(text)
        }
        
        override fun observe(owner: LifecycleOwner, observer: Observer<String>) {
            communication.observe(owner, observer)
        }
    }
}

interface InitCalls {
    
    fun initLocalView(view: SurfaceViewRenderer)
    
    fun initRemoteView(view: SurfaceViewRenderer)
}
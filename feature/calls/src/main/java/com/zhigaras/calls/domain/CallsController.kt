package com.zhigaras.calls.domain

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import androidx.core.content.ContextCompat
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import com.zhigaras.calls.domain.model.ConnectionData
import com.zhigaras.calls.domain.model.MyIceCandidate
import com.zhigaras.calls.domain.model.MySessionDescription
import com.zhigaras.calls.domain.model.ReadyToCallUser
import com.zhigaras.calls.webrtc.PeerConnectionCallback
import com.zhigaras.calls.webrtc.WebRtcClient
import com.zhigaras.cloudeservice.CloudService
import com.zhigaras.core.Dispatchers
import com.zhigaras.core.IntentAction
import com.zhigaras.messaging.domain.DataChannelCommunication
import com.zhigaras.messaging.domain.Messaging
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import org.webrtc.DataChannel
import org.webrtc.IceCandidate
import org.webrtc.MediaConstraints
import org.webrtc.MediaStream
import org.webrtc.PeerConnection.PeerConnectionState
import org.webrtc.SessionDescription
import org.webrtc.SurfaceViewRenderer

interface CallsController {
    
    fun sendInitialOffer()
    
    fun handleOffer(offer: SessionDescription, opponent: ReadyToCallUser)
    
    fun handleAnswer(answer: SessionDescription)
    
    fun setOpponent(opponent: ReadyToCallUser)
    
    fun handleIceCandidate(iceCandidate: MyIceCandidate)
    
    fun closeCurrentConnection()
    
    fun createNewConnection()
    
    fun closeConnectionTotally()
    
    fun onConnectionInterruptedByOpponent()
    
    fun sendInterruptionToOpponent()
    
    class Base(
        application: Context,
        dispatchers: Dispatchers,
        private val callsCloudService: CallsCloudService,
        private val peerConnectionCallback: PeerConnectionCallback,
        private val messagingCommunication: DataChannelCommunication.Mutable, //??
        private val webRtcClient: WebRtcClient //??
    ) : CallsController, InitCalls, Messaging {
        private var isConnected = false
        private var makingOffer = false
        private var isHandlingAnswer = false
        private var remoteView: SurfaceViewRenderer? = null //??
        private var localView: SurfaceViewRenderer? = null //??
        private var remoteMediaStream: MediaStream? = null //??
        private var user: ReadyToCallUser = ReadyToCallUser()
        private var opponent: ReadyToCallUser = ReadyToCallUser()
        private val scope = CoroutineScope(SupervisorJob() + dispatchers.default())
        private val connectionEventCallback = object : CloudService.Callback<ConnectionData> {
            override fun provide(data: ConnectionData) {
                data.handle(this@Base)
            }
            
            override fun error(message: String) {
                throw Exception(message)
            }
        }
        private val observer = Observer<com.zhigaras.calls.webrtc.PeerConnectionState> { state ->
            state.handle(ConnectionStateHandler())
        }
        
        inner class ConnectionStateHandler {
            
            fun onConnectionChanged(newState: PeerConnectionState) {
                peerConnectionCallback.invoke(newState)
                when (newState) {
                    PeerConnectionState.CONNECTED -> {
                        isConnected = true
                        callsCloudService.removeConnectionData(user.id)
                        callsCloudService.removeUserFromWaitList(opponent)
                    }
                    
                    else -> {
                        isConnected = false
                    }
                }
            }
            
            fun onIceCandidateCreated(iceCandidate: IceCandidate) {
                callsCloudService.sendToCloud(
                    ConnectionData(user, iceCandidate = MyIceCandidate(iceCandidate)), opponent.id
                )
            }
            
            fun onStreamAdded(mediaStream: MediaStream) {
                remoteMediaStream = mediaStream
                remoteMediaStream?.let {
                    val track = it.videoTracks
                    track[0].addSink(remoteView)
                }
            }
            
            fun onDataChannelCreated(dataChannel: DataChannel) {
                dataChannel.registerObserver(object : DataChannel.Observer {
                    override fun onBufferedAmountChange(p0: Long) = Unit
                    override fun onStateChange() = Unit
                    override fun onMessage(buffer: DataChannel.Buffer) {
                        val data = buffer.data
                        val bytes = ByteArray(data.remaining())
                        data[bytes]
                        messagingCommunication.postBackground(String(bytes))
                    }
                })
            }
        }
        
        init {
            webRtcClient.initNewConnection(observer)
            val connectionReceiver = object : BroadcastReceiver() {
                override fun onReceive(context: Context?, intent: Intent?) {
                    if (intent?.action == IntentAction.ACTION_NETWORK_STATE) {
                        val networkState = intent.getStringExtra("state")
                        val connState = webRtcClient.provideConnectionState()
                        if (
                            networkState == "online" &&
                            (connState == PeerConnectionState.DISCONNECTED || connState == PeerConnectionState.FAILED)
                        ) {
                            sendRestartOffer()
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
            // TODO: unregister this
        }
        
        override fun initLocalView(view: SurfaceViewRenderer) {
            localView = view
            webRtcClient.initLocalSurfaceView(view)
        }
        
        override fun initRemoteView(view: SurfaceViewRenderer) {
            webRtcClient.initRemoteSurfaceView(view)
            remoteView = view
            remoteMediaStream?.let {
                val track = it.videoTracks
                track[0].addSink(remoteView)
            }
        }
        
        override fun initUser(user: ReadyToCallUser) {
            this.user = user
        }
        
        override fun onConnectionInterruptedByOpponent() {
            peerConnectionCallback.postInterrupted()
            closeCurrentConnection()
            callsCloudService.removeInterruptionFlag(user.id)
        }
        
        override fun sendInterruptionToOpponent() {
            if (isConnected)
                callsCloudService
                    .sendToCloud(ConnectionData(user, interruptedByOpponent = true), opponent.id)
        }
        
        fun sendRestartOffer() {
            sendOffer(MediaConstraints().apply {
                mandatory.add(MediaConstraints.KeyValuePair("OfferToReceiveVideo", "true"))
                mandatory.add(MediaConstraints.KeyValuePair("IceRestart", "true"))
            })
        }
        
        override fun sendInitialOffer() {
            sendOffer(MediaConstraints().also {
                it.mandatory.add(MediaConstraints.KeyValuePair("OfferToReceiveVideo", "true"))
            })
        }
        
        private fun sendOffer(mediaConstraints: MediaConstraints) {
            makingOffer = true
            scope.launch {
                val offer = webRtcClient.createOffer(mediaConstraints)
                webRtcClient.setLocalDescription(offer)
                callsCloudService.sendToCloud(
                    ConnectionData(user, offer = MySessionDescription(offer)), opponent.id
                )
                makingOffer = false
            }
        }
        
        override fun handleOffer(offer: SessionDescription, opponent: ReadyToCallUser) {
            if (makingOffer) return
            this.opponent = opponent
            scope.launch {
                val mediaConstraints = MediaConstraints().also {
                    it.mandatory.add(MediaConstraints.KeyValuePair("OfferToReceiveVideo", "true"))
                }
                webRtcClient.setRemoteDescription(offer)
                val answer = webRtcClient.createAnswer(mediaConstraints)
                webRtcClient.setLocalDescription(answer)
                callsCloudService.sendToCloud(
                    ConnectionData(user, answer = MySessionDescription(answer)), opponent.id
                )
            }
        }
        
        override fun handleIceCandidate(iceCandidate: MyIceCandidate) {
            if (isHandlingAnswer) return // TODO: Probably loosing iceCandidates
            scope.launch {
                webRtcClient.addIceCandidate(iceCandidate)
            }
        }
        
        override fun handleAnswer(answer: SessionDescription) {
            scope.launch {
                isHandlingAnswer = true
                webRtcClient.setRemoteDescription(answer)
                isHandlingAnswer = false
            }
        }
        
        override fun setOpponent(opponent: ReadyToCallUser) {
            this.opponent = opponent
        }
        
        override fun createNewConnection() {
            webRtcClient.initNewConnection(observer)
            webRtcClient.addStreamTo(localView ?: return)
        }
        
        override fun closeCurrentConnection() {
            webRtcClient.closeCurrentConnection(observer)
            remoteMediaStream = null
            opponent = ReadyToCallUser()
            remoteView?.clearImage()
        }
        
        override fun closeConnectionTotally() {
            webRtcClient.closeConnectionTotally(observer)
            callsCloudService.removeCallback(connectionEventCallback)
            remoteMediaStream = null
            remoteView = null
            localView = null
            scope.cancel()
        }
        
        override fun subscribeToConnectionEvents(userId: String) {
            callsCloudService.observeUpdates(userId, connectionEventCallback)
        }
        
        override fun sendMessage(text: String) {
            webRtcClient.sendMessage(text)
        }
        
        override fun observe(owner: LifecycleOwner, observer: Observer<String>) {
            messagingCommunication.observe(owner, observer)
        }
    }
}

interface InitCalls {
    
    fun initLocalView(view: SurfaceViewRenderer)
    
    fun initRemoteView(view: SurfaceViewRenderer)
    
    fun initUser(user: ReadyToCallUser)
    
    fun subscribeToConnectionEvents(userId: String)
}
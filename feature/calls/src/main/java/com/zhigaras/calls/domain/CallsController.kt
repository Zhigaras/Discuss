package com.zhigaras.calls.domain

import android.util.Log
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import com.zhigaras.calls.domain.model.ConnectionData
import com.zhigaras.calls.domain.model.MyIceCandidate
import com.zhigaras.calls.domain.model.MySessionDescription
import com.zhigaras.calls.domain.model.ReadyToCallUser
import com.zhigaras.calls.webrtc.PeerConnectionCallback
import com.zhigaras.calls.webrtc.WebRtcClient
import com.zhigaras.cloudservice.CloudService
import com.zhigaras.core.Dispatchers
import com.zhigaras.core.NetworkHandler
import com.zhigaras.core.NetworkState
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
    
    fun sendInitialOffer(opponent: ReadyToCallUser)
    
    fun handleOffer(offer: SessionDescription, opponent: ReadyToCallUser)
    
    fun handleAnswer(answer: SessionDescription)
    
    fun handleIceCandidate(iceCandidate: MyIceCandidate)
    
    fun closeCurrentConnection()
    
    fun createNewConnection()
    
    fun closeConnectionTotally()
    
    fun onConnectionInterruptedByOpponent()
    
    fun sendInterruptionToOpponent()
    
    fun removeUserFromWaitList(user: ReadyToCallUser)
    
    class Base(
        dispatchers: Dispatchers,
        private val networkHandler: NetworkHandler,
        private val callsCloudService: CallsCloudService,
        private val peerConnectionCallback: PeerConnectionCallback,
        private val messagingCommunication: DataChannelCommunication.Mutable, //??
        private val webRtcClient: WebRtcClient //??
    ) : CallsController, InitCalls, Messaging {
        private var isConnected = false
        private var makingOffer = false
        private var isHandlingAnswer = false
        private var remoteView: SurfaceViewRenderer? = null //??
        private var remoteMediaStream: MediaStream? = null //??
        private var user: ReadyToCallUser = ReadyToCallUser()
        private var opponent: ReadyToCallUser = ReadyToCallUser()
        private val scope = CoroutineScope(SupervisorJob() + dispatchers.default())
        private val connectionEventCallback = object : CloudService.Callback<ConnectionData> {
            override fun provide(data: ConnectionData) {
//                opponent = data.opponent
                data.handle(this@Base)
            }
            
            override fun error(message: String) {
                throw Exception(message)
            }
        }
        private val observer = Observer<com.zhigaras.calls.webrtc.PeerConnectionState> { state ->
            state.handle(ConnectionStateHandler())
        }
        private val networkStateObserver = Observer<NetworkState> {
            when (it) {
                is NetworkState.Available -> {
                    val connState = webRtcClient.provideConnectionState()
                    if (connState == PeerConnectionState.DISCONNECTED ||
                        connState == PeerConnectionState.FAILED
                    ) {
                        peerConnectionCallback.postTryingToReconnect()
                        sendRestartOffer()
                    }
                }
                
                is NetworkState.Lost -> peerConnectionCallback.postCheckConnection()
            }
        }
        
        inner class ConnectionStateHandler {
            
            fun onConnectionChanged(newState: PeerConnectionState) {
                peerConnectionCallback.invoke(newState)
                when (newState) {
                    PeerConnectionState.CONNECTED -> {
                        isConnected = true
                        callsCloudService.removeConnectionData(user.id)
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
//                remoteMediaStream?.dispose()
                remoteMediaStream = mediaStream
                remoteMediaStream?.videoTracks?.get(0)?.addSink(remoteView)
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
            Log.d("AASSS", this.toString())
            Log.d("AASSS", webRtcClient.toString())
            webRtcClient.initNewConnection(observer)
            networkHandler.observeForever(networkStateObserver)
        }
        
        override fun initLocalView(view: SurfaceViewRenderer) {
            webRtcClient.initLocalSurfaceView(view)
        }
        
        override fun initRemoteView(view: SurfaceViewRenderer) {
            releaseRemoteView()
            remoteMediaStream?.videoTracks?.get(0)?.addSink(view)
            remoteView = view
            webRtcClient.initRemoteSurfaceView(view)
        }
        
        private fun releaseRemoteView() {
            remoteMediaStream?.videoTracks?.get(0)?.removeSink(remoteView)
            remoteView?.release()
            remoteView = null
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
        
        override fun removeUserFromWaitList(user: ReadyToCallUser) {
            callsCloudService.removeUserFromWaitList(user)
        }
        
        private fun sendRestartOffer() {
            sendOffer(MediaConstraints().apply {
                mandatory.add(MediaConstraints.KeyValuePair("OfferToReceiveVideo", "true"))
                mandatory.add(MediaConstraints.KeyValuePair("IceRestart", "true"))
            })
        }
        
        override fun sendInitialOffer(opponent: ReadyToCallUser) {
            this.opponent = opponent
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
//            if (isHandlingAnswer) return // TODO: Probably loosing iceCandidates
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
        
        override fun createNewConnection() {
            webRtcClient.initNewConnection(observer)
            webRtcClient.addLocalStream()
        }
        
        private fun commonCloseStuff() {
//            remoteMediaStream?.dispose()
//            remoteMediaStream = null
        }
        
        override fun closeCurrentConnection() {
            remoteMediaStream?.videoTracks?.get(0)?.removeSink(remoteView)
            remoteView?.clearImage()
            webRtcClient.closeCurrentConnection(observer)
            callsCloudService.removeOpponent(user.id)
            isConnected = false
            opponent = ReadyToCallUser()
            
//            webRtcClient.closeCurrentConnection(observer)
//            remoteMediaStream?.videoTracks?.get(0)?.removeSink(remoteView)
//            commonCloseStuff()
//            remoteView?.clearImage()
        }
        
        override fun closeConnectionTotally() {
            remoteMediaStream?.videoTracks?.get(0)?.removeSink(remoteView)
            remoteMediaStream?.videoTracks?.forEach { it.dispose() }
            remoteMediaStream?.audioTracks?.forEach { it.dispose() }
            remoteView?.release()
            remoteView = null
            callsCloudService.removeOpponent(user.id)
            isConnected = false
            opponent = ReadyToCallUser()
//            remoteMediaStream?.dispose()
            webRtcClient.closeConnectionTotally(observer)
//            releaseRemoteView()
//            commonCloseStuff()
//            remoteMediaStream?.dispose()
//            webRtcClient.closeConnectionTotally(observer)
            callsCloudService.removeCallback(connectionEventCallback)
            networkHandler.removeObserver(networkStateObserver)
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
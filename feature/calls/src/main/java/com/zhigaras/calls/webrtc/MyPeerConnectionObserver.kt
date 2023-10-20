package com.zhigaras.calls.webrtc

import android.os.Handler
import androidx.lifecycle.Observer
import org.webrtc.DataChannel
import org.webrtc.IceCandidate
import org.webrtc.MediaStream
import org.webrtc.PeerConnection
import org.webrtc.PeerConnection.IceConnectionState
import org.webrtc.PeerConnection.IceGatheringState
import org.webrtc.PeerConnection.SignalingState

@JvmInline
value class MainHandler(private val handler: Handler) {
    fun post(r: Runnable) = handler.post(r)
}

class MyPeerConnectionObserver(
    private val handler: MainHandler,
    private val communication: PeerConnectionCommunication.Mutable
) {
//    private val communication = FlowCommunication<PeerConnectionState>(MutableStateFlow(PeerConnectionState.Initial))
    
    fun observeForever(observer: Observer<PeerConnectionState>) {
        communication.observeForever(observer)
    }
    
    fun provideObserver() = observer
    
    private val observer = object : PeerConnection.Observer {
        override fun onSignalingChange(state: SignalingState) {
            handler.post {  communication.postUi(PeerConnectionState.SignallingChanged(state)) }
//            communication.postUi(PeerConnectionState.SignallingChanged(state))
        }
    
        override fun onConnectionChange(newState: PeerConnection.PeerConnectionState) {
            handler.post {  communication.postUi(PeerConnectionState.ConnectionChanged(newState)) }
//            communication.postUi(PeerConnectionState.ConnectionChanged(newState))
        }
        
        override fun onIceConnectionChange(newState: IceConnectionState) {
            handler.post {  communication.postUi(PeerConnectionState.IceConnectionChanged(newState)) }
//            communication.postUi(PeerConnectionState.IceConnectionChanged(newState))
        }
        
        override fun onIceConnectionReceivingChange(p0: Boolean) = Unit
        
        override fun onIceGatheringChange(newState: IceGatheringState) {
            handler.post {  communication.postUi(PeerConnectionState.IceGatheringChanged(newState)) }
//            communication.postUi(PeerConnectionState.IceGatheringChanged(newState))
        }
        
        override fun onIceCandidate(iceCandidate: IceCandidate) {
            handler.post {  communication.postUi(PeerConnectionState.IceCandidateCreated(iceCandidate)) }
//            communication.postUi(PeerConnectionState.IceCandidateCreated(iceCandidate))
        }
        
        override fun onIceCandidatesRemoved(iceCandidates: Array<out IceCandidate>?) {
            handler.post {  communication.postUi(PeerConnectionState.IceCandidatesRemoved(iceCandidates)) }
//            communication.postUi(PeerConnectionState.IceCandidatesRemoved(iceCandidates))
        }
        
        override fun onAddStream(mediaStream: MediaStream) {
            handler.post {  communication.postUi(PeerConnectionState.StreamAdded(mediaStream)) }
//            communication.postUi(PeerConnectionState.StreamAdded(mediaStream))
        }
        
        override fun onRemoveStream(mediaStream: MediaStream) {
            handler.post {  communication.postUi(PeerConnectionState.StreamRemoved(mediaStream)) }
//            communication.postUi(PeerConnectionState.StreamRemoved(mediaStream))
        }
        
        override fun onDataChannel(dataChannel: DataChannel) {
            handler.post {  communication.postUi(PeerConnectionState.DataChannelCreated(dataChannel)) }
//            communication.postUi(PeerConnectionState.DataChannelCreated(dataChannel))
        }
        
        override fun onRenegotiationNeeded() {
            handler.post {  communication.postUi(PeerConnectionState.RenegotiationNeeded()) }
//            communication.postUi(PeerConnectionState.RenegotiationNeeded())
        }
    }
}
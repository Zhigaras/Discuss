package com.zhigaras.calls.webrtc

import androidx.lifecycle.Observer
import com.zhigaras.core.Dispatchers
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import org.webrtc.DataChannel
import org.webrtc.IceCandidate
import org.webrtc.MediaStream
import org.webrtc.PeerConnection
import org.webrtc.PeerConnection.IceConnectionState
import org.webrtc.PeerConnection.IceGatheringState
import org.webrtc.PeerConnection.SignalingState

class MyPeerConnectionObserver(
    dispatchers: Dispatchers,
    private val communication: PeerConnectionCommunication.Mutable
) : PeerConnectionCommunication.ObserveForever {
    
    private val scope = CoroutineScope(dispatchers.main()) // TODO: close this scope
    override fun observeForever(observer: Observer<PeerConnectionState>) {
        communication.observeForever(observer)
    }
    
    override fun removeObserver(observer: Observer<PeerConnectionState>) {
        communication.removeObserver(observer)
    }
    
    fun provideObserver() = observer
    
    fun closeConnection() = scope.cancel()
    
    private val observer = object : PeerConnection.Observer {
        override fun onSignalingChange(state: SignalingState) {
            scope.launch { communication.postUi(PeerConnectionState.SignallingChanged(state)) }
        }
        
        override fun onConnectionChange(newState: PeerConnection.PeerConnectionState) {
            scope.launch { communication.postUi(PeerConnectionState.ConnectionChanged(newState)) }
        }
        
        override fun onIceConnectionChange(newState: IceConnectionState) {
            scope.launch { communication.postUi(PeerConnectionState.IceConnectionChanged(newState)) }
        }
        
        override fun onIceConnectionReceivingChange(p0: Boolean) = Unit
        
        override fun onIceGatheringChange(newState: IceGatheringState) {
            scope.launch { communication.postUi(PeerConnectionState.IceGatheringChanged(newState)) }
        }
        
        override fun onIceCandidate(iceCandidate: IceCandidate) {
            scope.launch { communication.postUi(PeerConnectionState.IceCandidateCreated(iceCandidate)) }
        }
        
        override fun onIceCandidatesRemoved(iceCandidates: Array<out IceCandidate>?) {
            scope.launch {
                communication.postUi(
                    PeerConnectionState.IceCandidatesRemoved(
                        iceCandidates
                    )
                )
            }
        }
        
        override fun onAddStream(mediaStream: MediaStream) {
            scope.launch { communication.postUi(PeerConnectionState.StreamAdded(mediaStream)) }
        }
        
        override fun onRemoveStream(mediaStream: MediaStream) {
            scope.launch { communication.postUi(PeerConnectionState.StreamRemoved(mediaStream)) }
        }
        
        override fun onDataChannel(dataChannel: DataChannel) {
            scope.launch { communication.postUi(PeerConnectionState.DataChannelCreated(dataChannel)) }
        }
        
        override fun onRenegotiationNeeded() {
            scope.launch { communication.postUi(PeerConnectionState.RenegotiationNeeded()) }
        }
    }
}
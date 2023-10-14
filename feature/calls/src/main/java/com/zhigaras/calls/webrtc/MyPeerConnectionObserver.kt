package com.zhigaras.calls.webrtc

import androidx.lifecycle.Observer
import org.webrtc.DataChannel
import org.webrtc.IceCandidate
import org.webrtc.MediaStream
import org.webrtc.PeerConnection
import org.webrtc.PeerConnection.IceConnectionState
import org.webrtc.PeerConnection.IceGatheringState
import org.webrtc.PeerConnection.SignalingState

class MyPeerConnectionObserver(
    private val communication: PeerConnectionCommunication.Mutable
) : PeerConnectionCommunication.ObserveForever {
    
    override fun observeForever(observer: Observer<PeerConnectionState>) {
        communication.observeForever(observer)
    }
    
    override fun removeObserver(observer: Observer<PeerConnectionState>) {
        communication.removeObserver(observer)
    }
    
    fun provideObserver() = observer
    
    private val observer = object : PeerConnection.Observer {
        override fun onSignalingChange(state: SignalingState) =
            communication.postBackground(PeerConnectionState.SignallingChanged(state))
    
        override fun onConnectionChange(newState: PeerConnection.PeerConnectionState) =
            communication.postBackground(PeerConnectionState.ConnectionChanged(newState))
        
        override fun onIceConnectionChange(newState: IceConnectionState) =
            communication.postBackground(PeerConnectionState.IceConnectionChanged(newState))
        
        override fun onIceConnectionReceivingChange(p0: Boolean) = Unit
        
        override fun onIceGatheringChange(newState: IceGatheringState?) =
            communication.postBackground(PeerConnectionState.IceGatheringChanged(newState))
        
        override fun onIceCandidate(iceCandidate: IceCandidate) =
            communication.postBackground(PeerConnectionState.IceCandidateCreated(iceCandidate))
        
        override fun onIceCandidatesRemoved(iceCandidates: Array<out IceCandidate>?) =
            communication.postBackground(PeerConnectionState.IceCandidatesRemoved(iceCandidates))
        
        override fun onAddStream(mediaStream: MediaStream) =
            communication.postBackground(PeerConnectionState.StreamAdded(mediaStream))
        
        override fun onRemoveStream(mediaStream: MediaStream) =
            communication.postBackground(PeerConnectionState.StreamRemoved(mediaStream))
        
        override fun onDataChannel(dataChannel: DataChannel) =
            communication.postBackground(PeerConnectionState.DataChannelCreated(dataChannel))
        
        override fun onRenegotiationNeeded() =
            communication.postBackground(PeerConnectionState.RenegotiationNeeded())
    }
}
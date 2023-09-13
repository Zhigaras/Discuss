package com.zhigaras.calls.webrtc

import org.webrtc.DataChannel
import org.webrtc.IceCandidate
import org.webrtc.MediaStream
import org.webrtc.PeerConnection

open class SimplePeerConnectionObserver : PeerConnection.Observer {
    
    override fun onSignalingChange(p0: PeerConnection.SignalingState?) {}
    
    override fun onIceConnectionChange(p0: PeerConnection.IceConnectionState?) {}
    
    override fun onIceConnectionReceivingChange(p0: Boolean) {}
    
    override fun onIceGatheringChange(p0: PeerConnection.IceGatheringState?) {}
    
    override fun onIceCandidate(iceCandidate: IceCandidate?) {}
    
    override fun onIceCandidatesRemoved(p0: Array<out IceCandidate>?) {}
    
    override fun onAddStream(mediaStream: MediaStream?) {}
    
    override fun onRemoveStream(p0: MediaStream?) {}
    
    override fun onDataChannel(p0: DataChannel?) {}
    
    override fun onRenegotiationNeeded() {}
}
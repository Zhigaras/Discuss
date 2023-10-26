package com.zhigaras.calls.webrtc

import android.util.Log
import com.zhigaras.calls.domain.CallsController.Base.ConnectionStateHandler
import org.webrtc.DataChannel
import org.webrtc.IceCandidate
import org.webrtc.MediaStream
import org.webrtc.PeerConnection

interface PeerConnectionState {
    
    fun handle(connectionStateHandler: ConnectionStateHandler)
    
    class SignallingChanged(private val newState: PeerConnection.SignalingState) :
        PeerConnectionState {
        override fun handle(connectionStateHandler: ConnectionStateHandler) {
            Log.d("QQQWW signal change", newState.toString())
        }
    }
    
    class ConnectionChanged(private val newState: PeerConnection.PeerConnectionState) :
        PeerConnectionState {
        override fun handle(connectionStateHandler: ConnectionStateHandler) {
            Log.d("QQQWW conn change", newState.toString())
            connectionStateHandler.onConnectionChanged(newState)
        }
    }
    
    class IceConnectionChanged(private val newState: PeerConnection.IceConnectionState) :
        PeerConnectionState {
        override fun handle(connectionStateHandler: ConnectionStateHandler) {
            Log.d("QQQWW ice conn change", newState.toString())
        }
    }
    
    class IceGatheringChanged(private val newState: PeerConnection.IceGatheringState) :
        PeerConnectionState {
        override fun handle(connectionStateHandler: ConnectionStateHandler) {
            Log.d("QQQWW gath change", newState.toString())
        }
    }
    
    class IceCandidateCreated(private val iceCandidate: IceCandidate) : PeerConnectionState {
        override fun handle(connectionStateHandler: ConnectionStateHandler) {
            Log.d("QQQWW ice created", iceCandidate.toString())
            connectionStateHandler.onIceCandidateCreated(iceCandidate)
        }
    }
    
    class IceCandidatesRemoved(private val iceCandidates: Array<out IceCandidate>?) :
        PeerConnectionState {
        override fun handle(connectionStateHandler: ConnectionStateHandler) {
            Log.d("QQQWW ice removed", iceCandidates.toString())
        }
    }
    
    class StreamAdded(private val mediaStream: MediaStream) : PeerConnectionState {
        override fun handle(connectionStateHandler: ConnectionStateHandler) {
            connectionStateHandler.onStreamAdded(mediaStream)
        }
    }
    
    class StreamRemoved(mediaStream: MediaStream) : PeerConnectionState {
        override fun handle(connectionStateHandler: ConnectionStateHandler) = Unit
    }
    
    class DataChannelCreated(private val dataChannel: DataChannel) : PeerConnectionState {
        override fun handle(connectionStateHandler: ConnectionStateHandler) {
            connectionStateHandler.onDataChannelCreated(dataChannel)
        }
    }
    
    class RenegotiationNeeded : PeerConnectionState {
        override fun handle(connectionStateHandler: ConnectionStateHandler) {
            Log.d("QQQWW", "renegotiaton needed")
        }
    }
}
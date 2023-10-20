package com.zhigaras.calls.webrtc

import com.zhigaras.calls.domain.CallsCloudService
import com.zhigaras.calls.domain.model.ConnectionData
import com.zhigaras.calls.domain.model.MyIceCandidate
import com.zhigaras.messaging.domain.DataChannelCommunication
import org.webrtc.DataChannel
import org.webrtc.IceCandidate
import org.webrtc.MediaStream
import org.webrtc.PeerConnection
import org.webrtc.SurfaceViewRenderer

interface PeerConnectionState {
    
    fun handle(
        remoteView: SurfaceViewRenderer?,
        peerConnectionCallback: PeerConnectionCallback,
        communication: DataChannelCommunication.Mutable,
        callsCloudService: CallsCloudService,
        target: String,
        userId: String
    )
    
    class SignallingChanged(private val newState: PeerConnection.SignalingState) :
        PeerConnectionState {
        override fun handle(
            remoteView: SurfaceViewRenderer?,
            peerConnectionCallback: PeerConnectionCallback,
            communication: DataChannelCommunication.Mutable,
            callsCloudService: CallsCloudService,
            target: String,
            userId: String
        ) = Unit
    }
    
    class ConnectionChanged(private val newState: PeerConnection.PeerConnectionState) :
        PeerConnectionState {
        override fun handle(
            remoteView: SurfaceViewRenderer?,
            peerConnectionCallback: PeerConnectionCallback,
            communication: DataChannelCommunication.Mutable,
            callsCloudService: CallsCloudService,
            target: String,
            userId: String
        ) {
            peerConnectionCallback.invoke(newState)
            if (newState == PeerConnection.PeerConnectionState.CONNECTED) {
                callsCloudService.removeConnectionData(userId)
            }
        }
    }
    
    class IceConnectionChanged(private val newState: PeerConnection.IceConnectionState) :
        PeerConnectionState {
        override fun handle(
            remoteView: SurfaceViewRenderer?,
            peerConnectionCallback: PeerConnectionCallback,
            communication: DataChannelCommunication.Mutable,
            callsCloudService: CallsCloudService,
            target: String,
            userId: String
        ) = Unit
    }
    
    class IceGatheringChanged(private val newState: PeerConnection.IceGatheringState) :
        PeerConnectionState {
        override fun handle(
            remoteView: SurfaceViewRenderer?,
            peerConnectionCallback: PeerConnectionCallback,
            communication: DataChannelCommunication.Mutable,
            callsCloudService: CallsCloudService,
            target: String,
            userId: String
        ) = Unit
    }
    
    class IceCandidateCreated(private val iceCandidate: IceCandidate) : PeerConnectionState {
        override fun handle(
            remoteView: SurfaceViewRenderer?,
            peerConnectionCallback: PeerConnectionCallback,
            communication: DataChannelCommunication.Mutable,
            callsCloudService: CallsCloudService,
            target: String,
            userId: String
        ) {
            callsCloudService.sendToCloud(
                ConnectionData(
                    target, userId, iceCandidate = MyIceCandidate(iceCandidate)
                )
            )
        }
    }
    
    class IceCandidatesRemoved(private val iceCandidates: Array<out IceCandidate>?) :
        PeerConnectionState {
        override fun handle(
            remoteView: SurfaceViewRenderer?,
            peerConnectionCallback: PeerConnectionCallback,
            communication: DataChannelCommunication.Mutable,
            callsCloudService: CallsCloudService,
            target: String,
            userId: String
        ) = Unit
    }
    
    class StreamAdded(private val mediaStream: MediaStream) : PeerConnectionState {
        override fun handle(
            remoteView: SurfaceViewRenderer?,
            peerConnectionCallback: PeerConnectionCallback,
            communication: DataChannelCommunication.Mutable,
            callsCloudService: CallsCloudService,
            target: String,
            userId: String
        ) {
            try {
                val track = mediaStream.videoTracks
                track[0].addSink(remoteView)
            } catch (e: Exception) {
                throw Exception("Can`t add stream")
            }
        }
    }
    
    class StreamRemoved(private val mediaStream: MediaStream) : PeerConnectionState {
        override fun handle(
            remoteView: SurfaceViewRenderer?,
            peerConnectionCallback: PeerConnectionCallback,
            communication: DataChannelCommunication.Mutable,
            callsCloudService: CallsCloudService,
            target: String,
            userId: String
        ) = Unit
    }
    
    class DataChannelCreated(private val dataChannel: DataChannel) : PeerConnectionState {
        override fun handle(
            remoteView: SurfaceViewRenderer?,
            peerConnectionCallback: PeerConnectionCallback,
            communication: DataChannelCommunication.Mutable,
            callsCloudService: CallsCloudService,
            target: String,
            userId: String
        ) {
            dataChannel.registerObserver(object : DataChannel.Observer {
                override fun onBufferedAmountChange(p0: Long) = Unit
                override fun onStateChange() = Unit
                override fun onMessage(buffer: DataChannel.Buffer) {
                    val data = buffer.data
                    val bytes = ByteArray(data.remaining())
                    data[bytes]
                    val text = String(bytes)
                    communication.postBackground(text)
                }
            })
            // TODO: unregister this
        }
    }
    
    class RenegotiationNeeded : PeerConnectionState {
        override fun handle(
            remoteView: SurfaceViewRenderer?,
            peerConnectionCallback: PeerConnectionCallback,
            communication: DataChannelCommunication.Mutable,
            callsCloudService: CallsCloudService,
            target: String,
            userId: String
        ) = Unit
    }
}
package com.zhigaras.calls.webrtc

import com.zhigaras.core.Dispatchers
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.launch
import org.webrtc.DataChannel
import org.webrtc.IceCandidate
import org.webrtc.MediaStream
import org.webrtc.PeerConnection
import org.webrtc.PeerConnection.IceConnectionState
import org.webrtc.PeerConnection.IceGatheringState
import org.webrtc.PeerConnection.SignalingState

class PeerConnectionObserveWrapper(
    dispatchers: Dispatchers,
    private val communication: PeerConnectionCommunication.Mutable
) : PeerConnectionCommunication.Observe {

    private val scope = CoroutineScope(dispatchers.main())

    override suspend fun collect(collector: FlowCollector<PeerConnectionState>) = communication.collect(collector)

    override fun current(): PeerConnectionState = communication.current()

    fun provideObserver() = observer

    fun closeConnection() = scope.cancel()

    private val observer = object : PeerConnection.Observer {
        override fun onSignalingChange(state: SignalingState) {
            scope.launch { communication.post(PeerConnectionState.SignallingChanged(state)) }
        }

        override fun onConnectionChange(newState: PeerConnection.PeerConnectionState) {
            scope.launch { communication.post(PeerConnectionState.ConnectionChanged(newState)) }
        }

        override fun onIceConnectionChange(newState: IceConnectionState) {
            scope.launch { communication.post(PeerConnectionState.IceConnectionChanged(newState)) }
        }

        override fun onIceConnectionReceivingChange(p0: Boolean) = Unit

        override fun onIceGatheringChange(newState: IceGatheringState) {
            scope.launch { communication.post(PeerConnectionState.IceGatheringChanged(newState)) }
        }

        override fun onIceCandidate(iceCandidate: IceCandidate) {
            scope.launch { communication.post(PeerConnectionState.IceCandidateCreated(iceCandidate)) }
        }

        override fun onIceCandidatesRemoved(iceCandidates: Array<out IceCandidate>?) {
            scope.launch {
                communication.post(PeerConnectionState.IceCandidatesRemoved(iceCandidates))
            }
        }

        override fun onAddStream(mediaStream: MediaStream) {
            scope.launch { communication.post(PeerConnectionState.StreamAdded(mediaStream)) }
        }

        override fun onRemoveStream(mediaStream: MediaStream) {
            scope.launch { communication.post(PeerConnectionState.StreamRemoved(mediaStream)) }
        }

        override fun onDataChannel(dataChannel: DataChannel) {
            scope.launch { communication.post(PeerConnectionState.DataChannelCreated(dataChannel)) }
        }

        override fun onRenegotiationNeeded() {
            scope.launch { communication.post(PeerConnectionState.RenegotiationNeeded()) }
        }
    }
}

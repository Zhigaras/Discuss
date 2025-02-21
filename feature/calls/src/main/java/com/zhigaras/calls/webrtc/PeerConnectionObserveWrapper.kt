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
    private val flux: PeerConnectionStateFlux.Mutable
) : PeerConnectionStateFlux.Observe {

    private val scope = CoroutineScope(dispatchers.main())

    override suspend fun collect(collector: FlowCollector<PeerConnectionState>) = flux.collect(collector)

    override fun current(): PeerConnectionState = flux.current()

    fun provideObserver() = observer

    fun closeConnection() = scope.cancel()

    private val observer = object : PeerConnection.Observer {
        override fun onSignalingChange(state: SignalingState) {
            scope.launch { flux.post(PeerConnectionState.SignallingChanged(state)) }
        }

        override fun onConnectionChange(newState: PeerConnection.PeerConnectionState) {
            scope.launch { flux.post(PeerConnectionState.ConnectionChanged(newState)) }
        }

        override fun onIceConnectionChange(newState: IceConnectionState) {
            scope.launch { flux.post(PeerConnectionState.IceConnectionChanged(newState)) }
        }

        override fun onIceConnectionReceivingChange(p0: Boolean) = Unit

        override fun onIceGatheringChange(newState: IceGatheringState) {
            scope.launch { flux.post(PeerConnectionState.IceGatheringChanged(newState)) }
        }

        override fun onIceCandidate(iceCandidate: IceCandidate) {
            scope.launch { flux.post(PeerConnectionState.IceCandidateCreated(iceCandidate)) }
        }

        override fun onIceCandidatesRemoved(iceCandidates: Array<out IceCandidate>?) {
            scope.launch {
                flux.post(PeerConnectionState.IceCandidatesRemoved(iceCandidates))
            }
        }

        override fun onAddStream(mediaStream: MediaStream) {
            scope.launch { flux.post(PeerConnectionState.StreamAdded(mediaStream)) }
        }

        override fun onRemoveStream(mediaStream: MediaStream) {
            scope.launch { flux.post(PeerConnectionState.StreamRemoved(mediaStream)) }
        }

        override fun onDataChannel(dataChannel: DataChannel) {
            scope.launch { flux.post(PeerConnectionState.DataChannelCreated(dataChannel)) }
        }

        override fun onRenegotiationNeeded() {
            scope.launch { flux.post(PeerConnectionState.RenegotiationNeeded()) }
        }
    }
}

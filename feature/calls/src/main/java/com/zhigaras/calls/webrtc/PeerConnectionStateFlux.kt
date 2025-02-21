package com.zhigaras.calls.webrtc

import com.zhigaras.core.StateFlux

interface PeerConnectionStateFlux {
    interface Observe : StateFlux.Observe<PeerConnectionState>
    interface Post : StateFlux.Post<PeerConnectionState>
    interface Mutable : StateFlux.Mutable<PeerConnectionState>, Post, Observe
    class Base : StateFlux.Base<PeerConnectionState>(PeerConnectionState.Initial()), Mutable
}

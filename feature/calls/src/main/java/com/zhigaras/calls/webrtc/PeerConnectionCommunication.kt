package com.zhigaras.calls.webrtc

import com.zhigaras.core.Communication

interface PeerConnectionCommunication {
    
    interface Observe : Communication.Observe<PeerConnectionState>
    interface ObserveForever : Communication.ObserveForever<PeerConnectionState>
    interface Post : Communication.Post<PeerConnectionState>
    interface Mutable : Communication.Mutable<PeerConnectionState>, Post, Observe
    class Base : Communication.Regular<PeerConnectionState>(), Mutable
}
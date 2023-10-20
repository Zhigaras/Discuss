package com.zhigaras.calls.webrtc

import com.zhigaras.core.Communication
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.flow.MutableStateFlow

interface PeerConnectionCommunication {
    
    interface Observe : Communication.Observe<PeerConnectionState>
    interface ObserveForever : Communication.ObserveForever<PeerConnectionState>
    interface Post : Communication.Post<PeerConnectionState>
    interface Mutable : Communication.Mutable<PeerConnectionState>, Post, Observe
    class Base : Communication.Regular<PeerConnectionState>(), Mutable
}

class FlowCommunication<T : Any>(private val flow: MutableStateFlow<T>) {
    
    fun post(item: T) {
        flow.value = item
    }
    
    suspend fun observe(observer: FlowCollector<T>) {
        flow.collect(observer)
    }
}
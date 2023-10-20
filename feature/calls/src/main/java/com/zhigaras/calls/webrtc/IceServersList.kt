package com.zhigaras.calls.webrtc

import org.webrtc.PeerConnection

@JvmInline
value class IceServersList(private val iceServers: List<PeerConnection.IceServer>) {
    fun provide() = iceServers
}
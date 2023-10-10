package com.zhigaras.calls.domain.model

import org.webrtc.IceCandidate

class MyIceCandidate(
    iceCandidate: IceCandidate? = null
) : IceCandidate(
    iceCandidate?.sdpMid ?: "",
    iceCandidate?.sdpMLineIndex ?: 0,
    iceCandidate?.sdp ?: ""
) {
    override fun toString(): String {
        return "sdpMid = $sdpMid, sdpMLineIndex = $sdpMLineIndex, sdp = $sdp"
    }
}
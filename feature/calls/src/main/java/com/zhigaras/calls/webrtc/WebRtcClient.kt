package com.zhigaras.calls.webrtc

interface WebRtcClient {
    
    fun startNegotiation(userId: String, opponentId: String)
}
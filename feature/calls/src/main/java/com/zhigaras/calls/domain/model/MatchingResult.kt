package com.zhigaras.calls.domain.model

import com.zhigaras.calls.webrtc.WebRtcClient

interface MatchingResult {
    
    fun isMatch(): Boolean
    
    suspend fun updateUi(webRtcClient: WebRtcClient)
    
    class Success(
        private val userId: String,
        private val opponentId: String,
        private val subjectId: String
    ) : MatchingResult {
        
        override fun isMatch() = true
        
        override suspend fun updateUi(webRtcClient: WebRtcClient) {
//            webRtcClient.startNegotiation(userId, opponentId)
        }
        
    }
    
    object NoMatch : MatchingResult {
        
        override fun isMatch() = false
        override suspend fun updateUi(webRtcClient: WebRtcClient) =
            Unit // TODO: handle not matching
        
    }
}

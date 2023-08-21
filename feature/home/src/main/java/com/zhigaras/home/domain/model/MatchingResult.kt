package com.zhigaras.home.domain.model

import com.zhigaras.home.domain.HomeInteractor

interface MatchingResult {
    
    fun isMatch(): Boolean
    
    suspend fun updateUi(homeInteractor: HomeInteractor)
    
    class Success(
        private val userId: String,
        private val opponentId: String,
        private val subjectId: String
    ) : MatchingResult {
        
        override fun isMatch() = true
        
        override suspend fun updateUi(homeInteractor: HomeInteractor) {
            homeInteractor.createRoom(userId, opponentId, subjectId)
        }
        
    }
    
    object NoMatch : MatchingResult {
        
        override fun isMatch() = false
        override suspend fun updateUi(homeInteractor: HomeInteractor) =
            Unit // TODO: handle not matching
        
    }
}

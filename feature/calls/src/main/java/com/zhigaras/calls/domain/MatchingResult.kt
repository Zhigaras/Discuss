package com.zhigaras.calls.domain

import com.zhigaras.calls.domain.model.DisputePosition

interface MatchingResult {
    
    fun isMatch(): Boolean
    
    suspend fun handle(
        callsController: CallsController,
        callsInteractor: CallsInteractor
    )
    
    abstract class Success(
        private val userId: String,
        private val opponentId: String,
        private val subjectId: String
    ) : MatchingResult {
        
        override fun isMatch() = true
        
        abstract val opponentOpinion: DisputePosition
        
        override suspend fun handle(
            callsController: CallsController,
            callsInteractor: CallsInteractor
        ) {
//            callsController.setOpponentId(opponentId)
//            callsInteractor.removeUserFromWaitList(subjectId, userId, opponentOpinion)
            callsController.startNegotiation(opponentId)
        }
    }
    
    class FoundUserWhoAgainst(
        userId: String,
        opponentId: String,
        subjectId: String
    ) : Success(userId, opponentId, subjectId) {
        
        override val opponentOpinion = DisputePosition.AGAINST
    }
    
    class FoundUserWhoSupport(
        userId: String,
        opponentId: String,
        subjectId: String
    ) : Success(userId, opponentId, subjectId) {
        
        override val opponentOpinion = DisputePosition.SUPPORT
    }
    
    class NoMatch(
        private val userId: String,
        private val subjectId: String,
        private val userOpinion: DisputePosition
    ) : MatchingResult {
        
        override fun isMatch() = false
        override suspend fun handle(
            callsController: CallsController,
            callsInteractor: CallsInteractor
        ) {
            callsInteractor.addUserToWaitList(subjectId, userId, userOpinion)
            callsController.subscribeToConnectionEvents(userId)
        }
    }
}

package com.zhigaras.calls.domain

import com.zhigaras.calls.domain.model.DisputePosition
import com.zhigaras.calls.ui.CallUiState

interface MatchingResult {
    
    fun isMatch(): Boolean
    
    suspend fun handle(
        callsController: CallsController,
        matchingInteractor: MatchingInteractor,
        communication: CallCommunication.Post,
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
            matchingInteractor: MatchingInteractor,
            communication: CallCommunication.Post
        ) {
            callsController.setOpponentId(opponentId)
            matchingInteractor.removeUserFromWaitList(subjectId, opponentId, opponentOpinion)
            callsController.startNegotiation(opponentId, userId)
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
            matchingInteractor: MatchingInteractor,
            communication: CallCommunication.Post
        ) {
            communication.postUi(CallUiState.WaitingForOpponent)
            matchingInteractor.addUserToWaitList(subjectId, userId, userOpinion)
            callsController.subscribeToConnectionEvents(userId)
        }
    }
}

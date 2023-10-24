package com.zhigaras.calls.domain

import com.zhigaras.calls.domain.model.DisputeParty
import com.zhigaras.calls.ui.CallUiState

interface MatchingResult {
    
    suspend fun handle(
        callsController: CallsController,
        matchingInteractor: MatchingInteractor,
        communication: CallCommunication.Post,
    )
    
    class OpponentFound(
        private val userId: String,
        private val opponentId: String,
        private val subjectId: String,
        private val opponentOpinion: DisputeParty
    ) : MatchingResult {
        
        override suspend fun handle(
            callsController: CallsController,
            matchingInteractor: MatchingInteractor,
            communication: CallCommunication.Post
        ) {
            callsController.setOpponentId(opponentId)
            matchingInteractor.removeUserFromWaitList(subjectId, opponentId, opponentOpinion)
            callsController.sendOffer(opponentId, userId)
        }
    }
    
    class NoMatch(
        private val userId: String,
        private val subjectId: String,
        private val userOpinion: DisputeParty
    ) : MatchingResult {
        
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

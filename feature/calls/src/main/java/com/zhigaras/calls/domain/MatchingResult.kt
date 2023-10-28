package com.zhigaras.calls.domain

import com.zhigaras.calls.domain.model.ReadyToCallUser
import com.zhigaras.calls.ui.CallUiState

interface MatchingResult {
    
    suspend fun handle(
        callsController: CallsController,
        matchingInteractor: MatchingInteractor,
        communication: CallCommunication.Post,
    )
    
    class OpponentFound(private val user: ReadyToCallUser) : MatchingResult {
        
        override suspend fun handle(
            callsController: CallsController,
            matchingInteractor: MatchingInteractor,
            communication: CallCommunication.Post
        ) {
            callsController.removeUserFromWaitList(user)
            callsController.sendInitialOffer(user)
        }
    }
    
    class NoMatch(private val user: ReadyToCallUser) : MatchingResult {
        
        override suspend fun handle(
            callsController: CallsController,
            matchingInteractor: MatchingInteractor,
            communication: CallCommunication.Post
        ) {
            communication.postUi(CallUiState.WaitingForOpponent())
            matchingInteractor.addUserToWaitList(user)
        }
    }
}

package com.zhigaras.calls.domain

import com.zhigaras.calls.domain.model.ReadyToCallUser
import com.zhigaras.calls.domain.model.Subject
import com.zhigaras.cloudservice.CloudService
import com.zhigaras.cloudservice.CloudService.Companion.SUBJECTS_PATH

interface MatchingInteractor {
    
    suspend fun addUserToWaitList(user: ReadyToCallUser)
    
    suspend fun checkMatching(user: ReadyToCallUser): MatchingResult
    
    class Base(private val cloudService: CloudService) : MatchingInteractor {
        
        override suspend fun addUserToWaitList(user: ReadyToCallUser) =
            user.addSelfToWaitList(cloudService)
        
        override suspend fun checkMatching(user: ReadyToCallUser): MatchingResult {
            return try {
                val subject = cloudService.getDataSnapshot(
                    Subject::class.java,
                    SUBJECTS_PATH,
                    user.subjectId.toString()
                )
                if (subject.hasOpponent(user.disputeParty!!)) {
                    val opponentId = subject.getOpponentId(user.disputeParty)
                    MatchingResult.OpponentFound(
                        ReadyToCallUser(opponentId, user.subjectId, user.disputeParty.opposite())
                    )
                } else {
                    MatchingResult.NoMatch(user)
                }
            } catch (e: Exception) {
                MatchingResult.Error(e.message ?: "Unknown error")
            }
        }
    }
}
package com.zhigaras.calls.domain

import com.zhigaras.calls.domain.model.DisputeParty
import com.zhigaras.calls.domain.model.ReadyToCallUser
import com.zhigaras.cloudservice.CloudService
import com.zhigaras.calls.domain.model.Subject
import com.zhigaras.cloudservice.CloudService.Companion.SUBJECTS_PATH

interface MatchingInteractor {
    
    suspend fun addUserToWaitList(user: ReadyToCallUser)
    
    suspend fun checkMatching(
        subjectId: String,
        userId: String,
        userOpinion: DisputeParty
    ): MatchingResult
    
    class Base(private val cloudService: CloudService) : MatchingInteractor {
        
        override suspend fun addUserToWaitList(user: ReadyToCallUser) =
            user.addSelfToWaitList(cloudService)
        
        override suspend fun checkMatching(
            subjectId: String,
            userId: String,
            userOpinion: DisputeParty
        ): MatchingResult {
            val subject =
                cloudService.getDataSnapshot(SUBJECTS_PATH, subjectId, Subject::class.java)
            return if (subject.hasOpponent(userOpinion)) {
                val opponentId = subject.getOpponentId(userOpinion)
                MatchingResult.OpponentFound(
                    ReadyToCallUser(opponentId, subjectId, userOpinion.opposite())
                )
            } else {
                MatchingResult.NoMatch(ReadyToCallUser(userId, subjectId, userOpinion))
            }
        }
    }
}
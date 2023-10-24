package com.zhigaras.calls.domain

import com.zhigaras.calls.domain.model.DisputeParty
import com.zhigaras.cloudeservice.CloudService
import com.zhigaras.calls.domain.model.Subject
import com.zhigaras.cloudeservice.CloudService.Companion.SUBJECTS_PATH

interface MatchingInteractor {
    
    suspend fun addUserToWaitList(
        subjectId: String,
        userId: String,
        userOpinion: DisputeParty
    )
    
    suspend fun checkMatching(
        subjectId: String,
        userId: String,
        userOpinion: DisputeParty,
        isConnectionRecreated: Boolean
    ): MatchingResult
    
    suspend fun removeUserFromWaitList(
        subjectId: String,
        userId: String,
        userOpinion: DisputeParty
    )
    
    class Base(private val cloudService: CloudService) : MatchingInteractor {
        
        override suspend fun addUserToWaitList(
            subjectId: String,
            userId: String,
            userOpinion: DisputeParty
        ) = cloudService.addItemToList(userId, SUBJECTS_PATH, subjectId, userOpinion.path)
        
        override suspend fun checkMatching(
            subjectId: String,
            userId: String,
            userOpinion: DisputeParty,
            isConnectionRecreated: Boolean
        ): MatchingResult {
            val subject =
                cloudService.getDataSnapshot(SUBJECTS_PATH, subjectId, Subject::class.java)
            return if (subject.hasOpponent(userOpinion)) {
                val opponentId = subject.getOpponentId(userOpinion)
                if (isConnectionRecreated) MatchingResult.NextOpponentFound(
                    userId,
                    opponentId,
                    subjectId,
                    userOpinion.opposite()
                )
                else MatchingResult.OpponentFound(
                    userId,
                    opponentId,
                    subjectId,
                    userOpinion.opposite()
                )
            } else {
                MatchingResult.NoMatch(userId, subjectId, userOpinion)
            }
        }
        
        override suspend fun removeUserFromWaitList(
            subjectId: String,
            userId: String,
            userOpinion: DisputeParty
        ) = cloudService.removeListItem(userId, SUBJECTS_PATH, subjectId, userOpinion.path)
    }
}
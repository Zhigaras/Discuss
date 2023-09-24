package com.zhigaras.calls.domain

import com.zhigaras.calls.domain.model.DisputePosition
import com.zhigaras.cloudeservice.CloudService
import com.zhigaras.calls.domain.model.Subject
import com.zhigaras.cloudeservice.CloudService.Companion.SUBJECTS_PATH

interface MatchingInteractor {
    
    suspend fun addUserToWaitList(
        subjectId: String,
        userId: String,
        userOpinion: DisputePosition
    )
    
    suspend fun checkMatching(
        subjectId: String,
        userId: String,
        userOpinion: DisputePosition
    ): MatchingResult
    
    suspend fun removeUserFromWaitList(
        subjectId: String,
        userId: String,
        userOpinion: DisputePosition
    )
    
    class Base(private val cloudService: CloudService) : MatchingInteractor {
        
        override suspend fun addUserToWaitList(
            subjectId: String,
            userId: String,
            userOpinion: DisputePosition
        ) {
            cloudService.addItemToList(userId, SUBJECTS_PATH, subjectId, userOpinion.path)
        }
        
        override suspend fun checkMatching(
            subjectId: String,
            userId: String,
            userOpinion: DisputePosition
        ): MatchingResult {
            val subject =
                cloudService.getDataSnapshot(SUBJECTS_PATH, subjectId, Subject::class.java)
            val opponentId: String
            return if (userOpinion == DisputePosition.AGAINST) {
                if (subject.supportList.isEmpty())
                    MatchingResult.NoMatch(userId, subjectId, userOpinion)
                else {
                    opponentId = subject.supportList.keys.first()
                    MatchingResult.FoundUserWhoSupport(userId, opponentId, subjectId)
                }
            } else {
                if (subject.againstList.isEmpty())
                    MatchingResult.NoMatch(userId, subjectId, userOpinion)
                else {
                    opponentId = subject.againstList.keys.first()
                    MatchingResult.FoundUserWhoAgainst(userId, opponentId, subjectId)
                }
            }
        }
        
        override suspend fun removeUserFromWaitList(
            subjectId: String,
            userId: String,
            userOpinion: DisputePosition
        ) {
            cloudService.removeListItem(userId, SUBJECTS_PATH, subjectId, userOpinion.path)
        }
    }
}
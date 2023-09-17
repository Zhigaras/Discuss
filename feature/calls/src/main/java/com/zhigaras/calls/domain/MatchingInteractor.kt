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
            cloudService.getListAndUpdate(
                path = SUBJECTS_PATH,
                child = subjectId,
                fieldId = userOpinion.path
            ) {
                add(userId)
                this
            }
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
                    opponentId = subject.supportList.first()
                    MatchingResult.FoundUserWhoSupport(userId, opponentId, subjectId)
                }
            } else {
                if (subject.againstList.isEmpty())
                    MatchingResult.NoMatch(userId, subjectId, userOpinion)
                else {
                    opponentId = subject.againstList.first()
                    MatchingResult.FoundUserWhoAgainst(userId, opponentId, subjectId)
                }
            }
        }
        
        override suspend fun removeUserFromWaitList(
            subjectId: String,
            userId: String,
            userOpinion: DisputePosition
        ) {
            cloudService.getListAndUpdate(SUBJECTS_PATH, subjectId, userOpinion.path) {
                remove(userId) // TODO: use remove() instead of getting and updating list?
                this
            }
        }
    }
}
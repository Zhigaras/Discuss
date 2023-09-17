package com.zhigaras.calls.domain

import com.zhigaras.calls.domain.model.DisputePosition
import com.zhigaras.cloudeservice.CloudService
import com.zhigaras.calls.domain.model.Subject

interface CallsInteractor {
    
    suspend fun addUserToWaitList(
        subjectId: String,
        userId: String,
        userOpinion: DisputePosition
    )
    
    fun subscribeToSubjects(callback: CloudService.Callback<Subject>)
    
    suspend fun checkMatching(
        subjectId: String,
        userId: String,
        userOpinion: DisputePosition
    ): MatchingResult
    
    class Base(private val cloudService: CloudService) : CallsInteractor {
        
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
        
        override fun subscribeToSubjects(callback: CloudService.Callback<Subject>) {
            cloudService.subscribeToRootLevel(SUBJECTS_PATH, Subject::class.java, callback)
        } // TODO: subscribe to single subject instead of list
    
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
        
        private suspend fun removeSingledUserFromWaitList(
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
    
    companion object {
        private const val SUBJECTS_PATH = "Subjects"
    }
}
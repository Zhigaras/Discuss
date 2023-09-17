package com.zhigaras.calls.domain

import com.zhigaras.calls.domain.model.DisputePosition
import com.zhigaras.cloudeservice.CloudService
import com.zhigaras.calls.domain.model.MatchingResult
import com.zhigaras.calls.domain.model.Subject

interface HomeInteractor {
    
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
    
    class Base(private val cloudService: CloudService) : HomeInteractor {
        
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
            val subject = cloudService
                .getDataSnapshot(SUBJECTS_PATH, subjectId, Subject::class.java)
            return try {
                val opponentId: String
                if (userOpinion == DisputePosition.AGAINST) {
                    opponentId = subject.supportList.first()
                    removeSingledUserFromWaitList(subjectId, opponentId, DisputePosition.SUPPORT)
                    removeSingledUserFromWaitList(subjectId, userId, DisputePosition.AGAINST)
                } else {
                    opponentId = subject.againstList.first()
                    removeSingledUserFromWaitList(subjectId, opponentId, DisputePosition.AGAINST)
                    removeSingledUserFromWaitList(subjectId, userId, DisputePosition.SUPPORT)
                }
                MatchingResult.Success(userId, opponentId, subjectId)
            } catch (e: NoSuchElementException) {
                MatchingResult.NoMatch
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
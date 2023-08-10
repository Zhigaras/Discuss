package com.zhigaras.home.domain

import com.zhigaras.cloudeservice.CloudService

interface HomeInteractor {
    
    suspend fun addUserToWaitList(subjectId: String, userId: String, position: DisputePosition)
    
    class Base(private val cloudService: CloudService) : HomeInteractor {
        
        override suspend fun addUserToWaitList(
            subjectId: String,
            userId: String,
            position: DisputePosition
        ) {
            cloudService.getAndUpdateField<MutableList<String>>(
                path = SUBJECTS_PATH,
                child = subjectId,
                fieldId = position.path
            ) {
                it.add(userId)
                it
            }
        }
    }
    
    companion object {
        private const val SUBJECTS_PATH = "Subjects"
    }
}
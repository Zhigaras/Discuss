package com.zhigaras.home.domain

import com.zhigaras.cloudeservice.CloudService
import com.zhigaras.home.domain.model.Subject

interface HomeInteractor {
    
    suspend fun addUserToWaitList(subjectId: String, userId: String, position: DisputePosition)
    
    fun subscribeToSubjects(callback: CloudService.Callback<Subject>)
    
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
        
        override fun subscribeToSubjects(callback: CloudService.Callback<Subject>) {
            cloudService.subscribeToRootLevel(SUBJECTS_PATH, Subject::class.java, callback)
        }
    }
    
    companion object {
        private const val SUBJECTS_PATH = "Subjects"
    }
}
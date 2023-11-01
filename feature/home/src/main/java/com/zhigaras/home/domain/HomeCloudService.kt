package com.zhigaras.home.domain

import com.zhigaras.cloudservice.CloudService
import com.zhigaras.home.domain.model.HomeSubject
import com.zhigaras.home.presentation.HomeUiState

interface HomeCloudService {
    
    fun subscribeToSubjects(callback: CloudService.Callback<List<HomeSubject>>)
    
    fun removeCallback(callback: CloudService.Callback<List<HomeSubject>>)
    
    suspend fun sendTopicSuggest(topic: String): HomeUiState
    
    companion object {
        const val SUBJECT_SUGGEST_PATH = "SubjectSuggest"
    }
}
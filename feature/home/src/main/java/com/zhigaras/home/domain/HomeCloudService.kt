package com.zhigaras.home.domain

import com.zhigaras.cloudservice.CloudService
import com.zhigaras.home.domain.model.HomeTopic
import com.zhigaras.home.presentation.home.HomeUiState

interface HomeCloudService {
    
    fun subscribeToTopics(callback: CloudService.Callback<List<HomeTopic>>)
    
    fun removeCallback(callback: CloudService.Callback<List<HomeTopic>>)
    
    suspend fun sendTopicSuggest(topic: String): HomeUiState
    
    companion object {
        const val TOPIC_SUGGEST_PATH = "TopicSuggest"
    }
}
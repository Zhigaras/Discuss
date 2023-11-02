package com.zhigaras.home.domain

import com.zhigaras.cloudservice.CloudService
import com.zhigaras.home.domain.model.HomeTopic
import com.zhigaras.home.presentation.suggesttopic.SuggestTopicUiState

interface HomeInteractor {
    
    fun subscribeToTopics(callback: CloudService.Callback<List<HomeTopic>>)
    
    fun removeCallback(callback: CloudService.Callback<List<HomeTopic>>)
    
    class Base(private val homeCloudService: HomeCloudService) : HomeInteractor, SuggestTopic {
        
        override fun subscribeToTopics(callback: CloudService.Callback<List<HomeTopic>>) {
            homeCloudService.subscribeToTopics(callback)
        }
        
        override fun removeCallback(callback: CloudService.Callback<List<HomeTopic>>) {
            homeCloudService.removeCallback(callback)
        }
        
        override suspend fun sendTopicSuggest(topic: String): SuggestTopicUiState {
            return kotlin.runCatching {
                homeCloudService.sendTopicSuggest(topic)
            }.fold(
                onSuccess = { SuggestTopicUiState.SuggestSuccessfullySent() },
                onFailure = { SuggestTopicUiState.SuggestSendingFailed(it.message) }
            )
        }
    }
}

interface SuggestTopic {
    
    suspend fun sendTopicSuggest(topic: String): SuggestTopicUiState
}
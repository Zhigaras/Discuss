package com.zhigaras.home.domain

import com.zhigaras.core.NetworkHandler
import com.zhigaras.core.NetworkState
import com.zhigaras.home.domain.model.HomeTopic
import com.zhigaras.home.presentation.suggesttopic.SuggestTopicUiState
import kotlinx.coroutines.flow.Flow

interface HomeInteractor {
    
    fun subscribeToTopics(): Flow<List<HomeTopic>>
    
    fun isOnline(): Boolean
    
    class Base(
        private val homeCloudService: HomeCloudService,
        private val networkHandler: NetworkHandler
    ) : HomeInteractor, SuggestTopic {
        
        override fun subscribeToTopics() = homeCloudService.subscribeToTopics()
        
        override suspend fun sendTopicSuggest(topic: String): SuggestTopicUiState {
            return kotlin.runCatching {
                homeCloudService.sendTopicSuggest(topic)
            }.fold(
                onSuccess = { SuggestTopicUiState.SuggestSuccessfullySent() },
                onFailure = { SuggestTopicUiState.SuggestSendingFailed(it.message) }
            )
        }
        
        override fun isOnline(): Boolean {
            val currentState = networkHandler.current()
            return currentState is NetworkState.Available || currentState is NetworkState.Loosing
        }
    }
}

interface SuggestTopic {
    
    suspend fun sendTopicSuggest(topic: String): SuggestTopicUiState
}
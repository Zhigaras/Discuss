package com.zhigaras.home.domain

import androidx.lifecycle.LifecycleOwner
import com.zhigaras.cloudservice.CloudService
import com.zhigaras.core.NetworkHandler
import com.zhigaras.core.NetworkState
import com.zhigaras.home.domain.model.HomeTopic
import com.zhigaras.home.presentation.HomeNetworkUiState
import com.zhigaras.home.presentation.suggesttopic.SuggestTopicUiState

interface HomeInteractor {
    
    fun subscribeToTopics(callback: CloudService.Callback<List<HomeTopic>>)
    
    fun removeCallback(callback: CloudService.Callback<List<HomeTopic>>)
    
    fun observeNetwork(owner: LifecycleOwner, communication: HomeCommunication.Post)
    
    class Base(
        private val homeCloudService: HomeCloudService,
        private val networkHandler: NetworkHandler
    ) : HomeInteractor, SuggestTopic {
        
        inner class HomeNetworkUiStateFactory {
            
            private val uiStates = listOf(
                HomeNetworkUiState.Available(),
                HomeNetworkUiState.Loosing(),
                HomeNetworkUiState.Lost(),
                HomeNetworkUiState.Unavailable()
            )
            
            fun state(networkState: NetworkState) = uiStates.find { it.matches(networkState) }!!
        }
        
        override fun subscribeToTopics(callback: CloudService.Callback<List<HomeTopic>>) {
            homeCloudService.subscribeToTopics(callback)
        }
        
        override fun removeCallback(callback: CloudService.Callback<List<HomeTopic>>) {
            homeCloudService.removeCallback(callback)
        }
        
        override fun observeNetwork(owner: LifecycleOwner, communication: HomeCommunication.Post) {
            networkHandler.observe(owner) {
                val uiState = HomeNetworkUiStateFactory().state(it)
                communication.postBackground(uiState)
            }
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
package com.zhigaras.home.domain

import com.zhigaras.home.domain.model.HomeTopic
import kotlinx.coroutines.flow.Flow

interface HomeCloudService {
    
    fun subscribeToTopics(): Flow<List<HomeTopic>>
    
    suspend fun sendTopicSuggest(topic: String): String
    
    companion object {
        const val TOPIC_SUGGEST_PATH = "TopicSuggest"
    }
}
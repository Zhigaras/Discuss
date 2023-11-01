package com.zhigaras.home.data

import com.zhigaras.cloudservice.CloudService
import com.zhigaras.cloudservice.CloudService.Companion.TOPICS_PATH
import com.zhigaras.home.domain.HomeCloudService
import com.zhigaras.home.domain.model.HomeTopic

class HomeCloudServiceImpl(private val cloudService: CloudService) : HomeCloudService {
    
    override fun subscribeToTopics(callback: CloudService.Callback<List<HomeTopic>>) {
        cloudService.subscribeToListMultipleLevels(callback, HomeTopic::class.java, TOPICS_PATH)
    }
    
    override fun removeCallback(callback: CloudService.Callback<List<HomeTopic>>) {
        cloudService.removeListener(callback)
    }
    
    override suspend fun sendTopicSuggest(topic: String): String {
        return cloudService.postWithIdGenerating(topic, HomeCloudService.TOPIC_SUGGEST_PATH)
    }
}
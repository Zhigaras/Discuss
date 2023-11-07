package com.zhigaras.home.data

import com.zhigaras.cloudservice.CloudService
import com.zhigaras.cloudservice.CloudService.Companion.TOPICS_PATH
import com.zhigaras.home.domain.HomeCloudService
import com.zhigaras.home.domain.model.HomeTopic
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withTimeout

class HomeCloudServiceImpl(private val cloudService: CloudService) : HomeCloudService {
    
    override fun subscribeToTopics(callback: CloudService.Callback<List<HomeTopic>>) {
        cloudService.subscribeToListMultipleLevels(callback, HomeTopic::class.java, TOPICS_PATH)
    }
    
    override fun removeCallback(callback: CloudService.Callback<List<HomeTopic>>) {
        cloudService.removeListener(callback)
    }
    
    override suspend fun sendTopicSuggest(topic: String): String {
        var result: String? = null
        withTimeout(SEND_SUGGEST_TIMEOUT) {
            CoroutineScope(Dispatchers.IO).launch {// TODO: close scope
                result =
                    cloudService.postWithIdGenerating(topic, HomeCloudService.TOPIC_SUGGEST_PATH)
            }.join()
        }
        return result!!
    }
    
    companion object {
        private const val SEND_SUGGEST_TIMEOUT = 5000L
    }
}
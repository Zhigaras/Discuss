package com.zhigaras.home.data

import com.zhigaras.cloudservice.CloudService
import com.zhigaras.cloudservice.CloudService.Companion.SUBJECTS_PATH
import com.zhigaras.home.domain.HomeCloudService
import com.zhigaras.home.domain.model.HomeSubject
import com.zhigaras.home.presentation.HomeUiState

class HomeCloudServiceImpl(private val cloudService: CloudService) : HomeCloudService {
    
    override fun subscribeToSubjects(callback: CloudService.Callback<List<HomeSubject>>) {
        cloudService.subscribeToListMultipleLevels(callback, HomeSubject::class.java, SUBJECTS_PATH)
    }
    
    override fun removeCallback(callback: CloudService.Callback<List<HomeSubject>>) {
        cloudService.removeListener(callback)
    }
    
    override suspend fun sendTopicSuggest(topic: String): HomeUiState {
        return kotlin.runCatching {
            cloudService.postWithIdGenerating(topic, HomeCloudService.SUBJECT_SUGGEST_PATH)
        }.fold(
            onSuccess = { HomeUiState.SuggestSuccessfullySent() },
            onFailure = { HomeUiState.SuggestSendingFailed(it.message) }
        )
    }
}
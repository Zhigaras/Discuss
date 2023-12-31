package com.zhigaras.home.presentation.suggesttopic

import com.zhigaras.core.BaseViewModel
import com.zhigaras.core.Dispatchers
import com.zhigaras.home.domain.SuggestTopic
import com.zhigaras.home.domain.SuggestTopicCommunication

class SuggestTopicViewModel(
    private val suggestTopic: SuggestTopic,
    override val uiCommunication: SuggestTopicCommunication.Mutable,
    dispatchers: Dispatchers
) : BaseViewModel<SuggestTopicUiState>(dispatchers) {
    
    fun sendSuggestion(topic: String) {
        uiCommunication.postUi(SuggestTopicUiState.Progress())
        scopeLaunch(
            onBackground = { suggestTopic.sendTopicSuggest(topic.trim()) },
            onUi = { uiCommunication.postUi(it) }
        )
    }
}
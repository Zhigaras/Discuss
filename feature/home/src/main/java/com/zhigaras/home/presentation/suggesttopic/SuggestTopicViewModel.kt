package com.zhigaras.home.presentation.suggesttopic

import com.zhigaras.core.BaseViewModel
import com.zhigaras.core.Dispatchers
import com.zhigaras.home.domain.SuggestTopic
import com.zhigaras.home.domain.SuggestTopicUiStateFlux

class SuggestTopicViewModel(
    private val suggestTopic: SuggestTopic,
    private val uiStateFlux: SuggestTopicUiStateFlux.Mutable,
    dispatchers: Dispatchers
) : BaseViewModel<SuggestTopicUiState>(dispatchers, uiStateFlux) {

    fun sendSuggestion(topic: String) {
        uiStateFlux.post(SuggestTopicUiState.Progress())
        scopeLaunch(
            onBackground = { suggestTopic.sendTopicSuggest(topic.trim()) },
            onUi = { uiStateFlux.post(it) }
        )
    }
}

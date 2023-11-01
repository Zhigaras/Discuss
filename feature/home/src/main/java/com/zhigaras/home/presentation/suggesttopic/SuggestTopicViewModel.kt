package com.zhigaras.home.presentation.suggesttopic

import com.zhigaras.core.BaseViewModel
import com.zhigaras.core.Communication
import com.zhigaras.core.Dispatchers
import com.zhigaras.home.domain.SuggestTopic

class SuggestTopicViewModel(
    private val suggestTopic: SuggestTopic,
    override val communication: Communication.Mutable<SuggestTopicUiState>,
    dispatchers: Dispatchers
) : BaseViewModel<SuggestTopicUiState>(dispatchers) {


}
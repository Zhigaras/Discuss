package com.zhigaras.home.presentation.suggesttopic

import com.zhigaras.core.BaseViewModel
import com.zhigaras.core.Communication
import com.zhigaras.core.Dispatchers

class SuggestTopicViewModel(
    override val communication: Communication.Mutable<SuggestTopicUiState>,
    dispatchers: Dispatchers
) : BaseViewModel<SuggestTopicUiState>(dispatchers) {


}
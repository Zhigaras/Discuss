package com.zhigaras.home.presentation.suggesttopic

import com.zhigaras.core.UiState
import com.zhigaras.home.databinding.DialogSuggestTopicBinding

interface SuggestTopicUiState : UiState<DialogSuggestTopicBinding> {
    
    class SuggestSuccessfullySent : SuggestTopicUiState {
        
        override fun update(binding: DialogSuggestTopicBinding) {
            TODO("Not yet implemented")
        }
    }
    
    class SuggestSendingFailed(private val msg: String?) : SuggestTopicUiState {
        
        override fun update(binding: DialogSuggestTopicBinding) {
            TODO("Not yet implemented")
        }
    }
}
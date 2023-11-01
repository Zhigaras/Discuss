package com.zhigaras.home.presentation.suggesttopic

import android.util.Log
import com.zhigaras.core.UiState
import com.zhigaras.home.databinding.DialogSuggestTopicBinding

interface SuggestTopicUiState : UiState<DialogSuggestTopicBinding> {
    
    class SuggestSuccessfullySent : SuggestTopicUiState {
        
        override fun update(binding: DialogSuggestTopicBinding) {
            Log.d("QQQWW", "success")
        }
    }
    
    class SuggestSendingFailed(private val msg: String?) : SuggestTopicUiState {
        
        override fun update(binding: DialogSuggestTopicBinding) {
            Log.d("QQQWW", "error")
        }
    }
}
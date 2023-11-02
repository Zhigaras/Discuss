package com.zhigaras.home.presentation.suggesttopic

import android.view.View
import com.zhigaras.core.UiState
import com.zhigaras.home.databinding.DialogSuggestTopicBinding

interface SuggestTopicUiState : UiState<DialogSuggestTopicBinding> {
    
    class Progress : SuggestTopicUiState {
        
        override fun update(binding: DialogSuggestTopicBinding) {
            binding.initialView.visibility = View.VISIBLE
            binding.failedView.root.visibility = View.GONE
            binding.successView.root.visibility = View.GONE
            binding.sendSuggestionButton.visibility = View.GONE
            binding.progressBar.visibility = View.VISIBLE
        }
    }
    
    class SuggestSuccessfullySent : SuggestTopicUiState {
        
        override fun update(binding: DialogSuggestTopicBinding) {
            binding.failedView.root.visibility = View.GONE
            binding.successView.root.visibility = View.VISIBLE
            binding.initialView.visibility = View.GONE
            binding.progressBar.visibility = View.GONE
        }
    }
    
    class SuggestSendingFailed(private val msg: String?) : SuggestTopicUiState {
        
        override fun update(binding: DialogSuggestTopicBinding) {
            binding.failedView.root.visibility = View.VISIBLE
            binding.successView.root.visibility = View.GONE
            binding.initialView.visibility = View.GONE
            binding.progressBar.visibility = View.GONE
        }
    }
}
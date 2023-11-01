package com.zhigaras.home.presentation.suggesttopic

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import com.zhigaras.core.BaseDialog
import com.zhigaras.home.databinding.DialogSuggestTopicBinding
import org.koin.androidx.viewmodel.ext.android.viewModel

class SuggestTopicBottomSheetDialog : BaseDialog<DialogSuggestTopicBinding>() {
    
    private val viewModel: SuggestTopicViewModel by viewModel()
    
    override fun initBinding(inflater: LayoutInflater) = DialogSuggestTopicBinding.inflate(inflater)
    
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        
        binding.sendSuggestionButton.setOnClickListener {
            trySend()
        }
        
        viewModel.observe(this) {
            it.update(binding)
        }
        
        binding.successView.dismissButton.setOnClickListener {
            dismiss()
        }
        
        binding.failedView.retryButton.setOnClickListener {
            trySend()
        }
    }
    
    private fun trySend() {
        if (binding.topicInputLayout.isValid())
            viewModel.sendSuggestion(binding.topicInputLayout.text())
    }
}
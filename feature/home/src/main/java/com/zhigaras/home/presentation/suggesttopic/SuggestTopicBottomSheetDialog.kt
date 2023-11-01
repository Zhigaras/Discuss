package com.zhigaras.home.presentation.suggesttopic

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import com.zhigaras.core.BaseDialog
import com.zhigaras.home.databinding.DialogSuggestTopicBinding
import org.koin.androidx.viewmodel.ext.android.activityViewModel

class SuggestTopicBottomSheetDialog : BaseDialog<DialogSuggestTopicBinding>() {
    
    private val viewModel: SuggestTopicViewModel by activityViewModel()
    
    override fun initBinding(inflater: LayoutInflater) = DialogSuggestTopicBinding.inflate(inflater)
    
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }
}
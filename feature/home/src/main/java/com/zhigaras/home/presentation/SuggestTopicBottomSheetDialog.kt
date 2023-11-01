package com.zhigaras.home.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import com.zhigaras.core.BaseDialog
import com.zhigaras.home.databinding.DialogSuggestTopicBinding

class SuggestTopicBottomSheetDialog : BaseDialog<DialogSuggestTopicBinding>() {
    
    override fun initBinding(inflater: LayoutInflater) = DialogSuggestTopicBinding.inflate(inflater)
    
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        
    }
}
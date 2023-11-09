package com.zhigaras.core

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.annotation.StringRes
import androidx.core.os.bundleOf
import com.zhigaras.core.databinding.BaseAlertDialogBinding

abstract class BaseAlertDialog : BaseDialog<BaseAlertDialogBinding>() {
    
    @get:StringRes
    protected abstract val alertText: Int
    
    override fun initBinding(inflater: LayoutInflater) = BaseAlertDialogBinding.inflate(inflater)
    
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        
        val requestKey = arguments?.getString(REQUEST_KEY) ?: ""
        
        binding.alertText.setText(alertText)
        binding.dismissButton.setOnClickListener {
            parentFragmentManager.setFragmentResult(requestKey, bundleOf(PARAM_KEY to false))
            dismiss()
        }
        
        binding.positiveButton.setOnClickListener {
            parentFragmentManager.setFragmentResult(requestKey, bundleOf(PARAM_KEY to true))
            dismiss()
        }
    }
    
    companion object {
        const val REQUEST_KEY = "requestKey"
        const val PARAM_KEY = "paramKey"
    }
}
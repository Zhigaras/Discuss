package com.zhigaras.core

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.viewbinding.ViewBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

abstract class BaseDialog<VB : ViewBinding> : BottomSheetDialogFragment() {
    
    private var _binding: VB? = null
    protected val binding get() = _binding!!
    
    protected abstract fun initBinding(inflater: LayoutInflater): VB
    
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = initBinding(inflater)
        return binding.root
    }
    
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
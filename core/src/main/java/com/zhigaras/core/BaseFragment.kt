package com.zhigaras.core

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding

abstract class BaseFragment<B : ViewBinding> : Fragment() {
    
    private var _binding: B? = null
    protected val binding get() = _binding!!
    
    protected open val canHandleBackPress = false
    
    protected open val backPressedCallback = {}
    
    protected abstract fun initBinding(inflater: LayoutInflater): B
    
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        _binding = initBinding(inflater)
        return binding.root
    }
    
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (canHandleBackPress)
            activity?.onBackPressedDispatcher?.addCallback(
                viewLifecycleOwner,
                object : OnBackPressedCallback(true) {
                    override fun handleOnBackPressed() = backPressedCallback.invoke()
                })
    }
    
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
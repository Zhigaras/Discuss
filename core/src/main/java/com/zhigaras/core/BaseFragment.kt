package com.zhigaras.core

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding
import org.koin.androidx.viewmodel.ext.android.viewModel

abstract class BaseFragment<B : ViewBinding, S : UiState> : Fragment() {
    
    private var _binding: B? = null
    protected val binding get() = _binding!!
    
    inline fun <reified VM : BaseViewModel<S>> initViewModel(): VM {
        val viewModel by viewModel<VM>()
        return viewModel
    }
    
    protected abstract fun initBinding(inflater: LayoutInflater): B
    
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        _binding = initBinding(inflater)
        return binding.root
    }
    
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
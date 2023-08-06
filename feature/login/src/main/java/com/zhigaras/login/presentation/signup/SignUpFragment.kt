package com.zhigaras.login.presentation.signup

import android.view.LayoutInflater
import com.zhigaras.core.BaseFragment
import com.zhigaras.login.databinding.FragmentSignUpBinding

class SignUpFragment : BaseFragment<FragmentSignUpBinding, SignUpUiState>() {
    
    override fun initBinding(inflater: LayoutInflater) = FragmentSignUpBinding.inflate(inflater)
    
    private val viewModel by lazy { initViewModel<SignUpViewModel>() }
    
}
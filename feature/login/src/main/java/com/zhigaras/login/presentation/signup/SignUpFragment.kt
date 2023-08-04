package com.zhigaras.login.presentation.signup

import android.view.LayoutInflater
import com.zhigaras.core.presentation.BaseFragment
import com.zhigaras.login.databinding.FragmentSignUpBinding

class SignUpFragment : BaseFragment<FragmentSignUpBinding, SignUpState>() {
    
    override fun initBinding(inflater: LayoutInflater) = FragmentSignUpBinding.inflate(inflater)
    
    private val viewModel by lazy { initViewModel<SignUpViewModel>() }
    
}
package com.zhigaras.login.presentation.signin

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import com.zhigaras.core.BaseFragment
import com.zhigaras.login.databinding.FragmentSignInBinding

class SignInFragment : BaseFragment<FragmentSignInBinding, SignInUiState>() {
    
    private val viewModel by lazy { initViewModel<SignInViewModel>() }
    
    override fun initBinding(inflater: LayoutInflater) = FragmentSignInBinding.inflate(inflater)
    
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        
        val inputList = listOf(binding.emailInput.inputLayout, binding.passwordInput.inputLayout)
        
        binding.signInWithPassword.setOnClickListener {
            val isAllValid = inputList.map { it.isValid() }.all { it }
            if (isAllValid) {
                viewModel.signIn(inputList.first().text(), inputList.last().text())
            }
        }
        binding.signUpWithPassword.setOnClickListener {
            viewModel.navigateToSignUp()
        }
        
        viewModel.observe(this) {
            it?.update(binding.progressLayout)
        }
    }
}
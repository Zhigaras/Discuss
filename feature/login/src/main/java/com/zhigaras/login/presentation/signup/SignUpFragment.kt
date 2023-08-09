package com.zhigaras.login.presentation.signup

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import com.zhigaras.core.BaseFragment
import com.zhigaras.login.databinding.FragmentSignUpBinding

class SignUpFragment : BaseFragment<FragmentSignUpBinding, SignUpUiState>() {
    
    override fun initBinding(inflater: LayoutInflater) = FragmentSignUpBinding.inflate(inflater)
    
    private val viewModel by lazy { initViewModel<SignUpViewModel>() }
    
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        
        val inputList = listOf(binding.emailInputLayout.root, binding.passwordInputLayout)
        
        binding.signUp.setOnClickListener {
            val isValid = inputList.map { it.isValid() }
            if (isValid.all { it })
                viewModel.signUp(
                    binding.emailInputLayout.root.text(),
                    binding.passwordInputLayout.text()
                )
        }
        
        viewModel.observe(this) {
            it?.update(binding.progressLayout.root)
        }
    }
}
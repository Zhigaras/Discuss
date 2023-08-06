package com.zhigaras.login.presentation.signin

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import com.zhigaras.core.BaseFragment
import com.zhigaras.login.databinding.FragmentSignInBinding

class SignInFragment : BaseFragment<FragmentSignInBinding, SignInUiState>() {
    
    private val viewModel by lazy { initViewModel<SignInViewModel>() }
    
    override fun initBinding(inflater: LayoutInflater) = FragmentSignInBinding.inflate(inflater)
    
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        
        binding.signInWithPassword.setOnClickListener {
            viewModel.signIn(
                binding.emailInput.inputLayout.text(),
                binding.passwordInput.inputLayout.text()
            )
        }
        
        viewModel.observe(this) {
            Log.d("AAA from fragment", it.toString())
            
        }
    }
}
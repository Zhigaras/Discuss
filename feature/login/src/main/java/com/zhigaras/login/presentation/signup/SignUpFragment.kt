package com.zhigaras.login.presentation.signup

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import com.zhigaras.core.BaseFragment
import com.zhigaras.login.databinding.FragmentSignUpBinding
import com.zhigaras.login.domain.LoginRoutes
import org.koin.androidx.viewmodel.ext.android.viewModel

class SignUpFragment : BaseFragment<FragmentSignUpBinding>() {
    
    override fun initBinding(inflater: LayoutInflater) = FragmentSignUpBinding.inflate(inflater)
    
    private val viewModel by viewModel<SignUpViewModel>()
    
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        
        arguments?.let { binding.emailInputLayout.root.setText(it.getString(LoginRoutes.EMAIL_KEY)) }
        
        val inputList = listOf(binding.emailInputLayout.root, binding.passwordInputLayout.root)
        
        binding.signUp.setOnClickListener {
            val isValid = inputList.map { it.isValid() }
            if (isValid.all { it })
                viewModel.signUp(inputList.first().text(), inputList.last().text())
        }
        
        viewModel.observe(this) {
            it?.update(binding)
        }
    }
}
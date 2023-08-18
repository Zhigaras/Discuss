package com.zhigaras.login.presentation.resetpassword

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import com.zhigaras.core.BaseDialog
import com.zhigaras.login.databinding.DialogResetPasswordBinding
import com.zhigaras.login.domain.LoginRoutes
import com.zhigaras.login.presentation.input.EmailInput
import org.koin.androidx.viewmodel.ext.android.viewModel

class ResetPasswordDialog : BaseDialog<DialogResetPasswordBinding>() {
    
    private val viewModel by viewModel<ResetPasswordViewModel>()
    private lateinit var emailInput: EmailInput
    
    override fun initBinding(inflater: LayoutInflater) =
        DialogResetPasswordBinding.inflate(inflater)
    
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        emailInput = binding.emailInput.root
        arguments?.let { emailInput.setText(it.getString(LoginRoutes.EMAIL_KEY)) }
        
        binding.resetPassword.setOnClickListener {
            if (emailInput.isValid()) viewModel.resetPassword(emailInput.text())
        }
    }
}
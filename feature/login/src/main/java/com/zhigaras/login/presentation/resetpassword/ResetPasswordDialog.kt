package com.zhigaras.login.presentation.resetpassword

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import com.zhigaras.core.BaseDialog
import com.zhigaras.login.databinding.DialogResetPasswordBinding
import com.zhigaras.login.domain.LoginRoutes
import org.koin.androidx.viewmodel.ext.android.viewModel

class ResetPasswordDialog : BaseDialog<DialogResetPasswordBinding>() {
    
    private val viewModel by viewModel<ResetPasswordViewModel>()
    
    override fun initBinding(inflater: LayoutInflater) =
        DialogResetPasswordBinding.inflate(inflater)
    
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val emailInput = binding.emailInput.root
        arguments?.let { emailInput.setText(it.getString(LoginRoutes.EMAIL_KEY)) }
        
        binding.resetPasswordButton.setOnClickListener {
            if (emailInput.isValid()) viewModel.resetPassword(emailInput.text())
        }
        binding.closeDialogButton.setOnClickListener {
            this.dismiss()
            viewModel.setInitialState()
        }
        viewModel.observe(this) {
            it.update(binding)
        }
    }
}
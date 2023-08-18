package com.zhigaras.login.presentation.resetpassword

import androidx.annotation.StringRes
import com.zhigaras.core.UiState
import com.zhigaras.login.databinding.DialogResetPasswordBinding

interface ResetPasswordUiState : UiState<DialogResetPasswordBinding> {
    
    object Progress : ResetPasswordUiState {
        
        override fun update(binding: DialogResetPasswordBinding) {
            // TODO: make
        }
    }
    
    object Success : ResetPasswordUiState {
        
        override fun update(binding: DialogResetPasswordBinding) {
            // TODO: make
        }
    }
    
    class SingleEventError(@StringRes val messageId: Int) : ResetPasswordUiState,
        UiState.SingleEvent<DialogResetPasswordBinding>() {
        override val block: (DialogResetPasswordBinding) -> Unit = {
            // TODO: make
        }
    }
}
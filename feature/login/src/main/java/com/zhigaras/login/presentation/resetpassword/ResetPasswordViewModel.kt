package com.zhigaras.login.presentation.resetpassword

import com.zhigaras.auth.Auth
import com.zhigaras.core.BaseViewModel
import com.zhigaras.core.Dispatchers
import com.zhigaras.login.databinding.DialogResetPasswordBinding
import com.zhigaras.login.domain.ResetPasswordCommunication

class ResetPasswordViewModel(
    private val auth: Auth,
    communication: ResetPasswordCommunication.Mutable,
    dispatchers: Dispatchers
) : BaseViewModel<DialogResetPasswordBinding, ResetPasswordUiState>(communication, dispatchers) {
    
    fun resetPassword(email: String) = scopeLaunch(
        onLoading = { communication.post(ResetPasswordUiState.Progress) },
        onSuccess = { communication.post(ResetPasswordUiState.Success) },
        onError = { communication.post(ResetPasswordUiState.SingleEventError(it.errorId())) }
    ) {
        auth.resetPassword(email)
    }
    
    fun setInitialState() = communication.post(ResetPasswordUiState.Initial)
}
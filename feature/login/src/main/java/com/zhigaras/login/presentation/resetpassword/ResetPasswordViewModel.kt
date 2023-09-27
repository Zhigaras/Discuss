package com.zhigaras.login.presentation.resetpassword

import com.zhigaras.auth.Auth
import com.zhigaras.core.BaseViewModel
import com.zhigaras.core.Dispatchers
import com.zhigaras.login.databinding.DialogResetPasswordBinding
import com.zhigaras.login.presentation.resetpassword.domain.ResetPasswordCommunication

class ResetPasswordViewModel(
    private val auth: Auth,
    override val communication: ResetPasswordCommunication.Mutable,
    dispatchers: Dispatchers
) : BaseViewModel<DialogResetPasswordBinding, ResetPasswordUiState>(dispatchers) {
    
    fun resetPassword(email: String) = scopeLaunch(
        onLoading = { communication.postUi(ResetPasswordUiState.Progress) },
        onSuccess = { communication.postUi(ResetPasswordUiState.Success) },
        onError = { communication.postUi(ResetPasswordUiState.SingleEventError(it.errorId())) }
    ) {
        auth.resetPassword(email)
    }
    
    fun setInitialState() = communication.postUi(ResetPasswordUiState.Initial)
}
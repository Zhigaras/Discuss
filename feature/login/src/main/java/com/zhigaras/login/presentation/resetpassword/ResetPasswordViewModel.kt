package com.zhigaras.login.presentation.resetpassword

import com.zhigaras.core.BaseViewModel
import com.zhigaras.core.Dispatchers
import com.zhigaras.login.databinding.DialogResetPasswordBinding
import com.zhigaras.login.presentation.resetpassword.domain.ResetPasswordCommunication
import com.zhigaras.login.presentation.resetpassword.domain.ResetPasswordRepository

class ResetPasswordViewModel(
    private val resetPasswordRepository: ResetPasswordRepository,
    override val communication: ResetPasswordCommunication.Mutable,
    dispatchers: Dispatchers
) : BaseViewModel<DialogResetPasswordBinding, ResetPasswordUiState>(dispatchers) {
    
    fun resetPassword(email: String) = scopeLaunch({
        communication.postUi(ResetPasswordUiState.Progress)
        resetPasswordRepository.resetPassword(email)
    }) {
        it.handle(communication)
    }
    
    fun setInitialState() = communication.postUi(ResetPasswordUiState.Initial)
}
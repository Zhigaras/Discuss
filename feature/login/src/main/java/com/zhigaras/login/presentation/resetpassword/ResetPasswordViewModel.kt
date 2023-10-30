package com.zhigaras.login.presentation.resetpassword

import com.zhigaras.core.BaseViewModel
import com.zhigaras.core.Dispatchers
import com.zhigaras.login.domain.resetpassword.ResetPasswordCommunication
import com.zhigaras.login.domain.resetpassword.ResetPasswordRepository

class ResetPasswordViewModel(
    private val resetPasswordRepository: ResetPasswordRepository,
    override val communication: ResetPasswordCommunication.Mutable,
    dispatchers: Dispatchers
) : BaseViewModel<ResetPasswordUiState>(dispatchers) {
    
    fun resetPassword(email: String) {
        communication.postUi(ResetPasswordUiState.Progress)
        scopeLaunch({
            resetPasswordRepository.resetPassword(email)
        }) {
            it.handle(communication)
        }
    }
    
    fun setInitialState() = communication.postUi(ResetPasswordUiState.Initial)
}
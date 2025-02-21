package com.zhigaras.login.presentation.resetpassword

import com.zhigaras.core.BaseViewModel
import com.zhigaras.core.Dispatchers
import com.zhigaras.login.domain.resetpassword.ResetPasswordUiStateFlux
import com.zhigaras.login.domain.resetpassword.ResetPasswordRepository

class ResetPasswordViewModel(
    private val resetPasswordRepository: ResetPasswordRepository,
    private val uiStateFlux: ResetPasswordUiStateFlux.Mutable,
    dispatchers: Dispatchers
) : BaseViewModel<ResetPasswordUiState>(dispatchers, uiStateFlux) {

    fun resetPassword(email: String) {
        uiStateFlux.post(ResetPasswordUiState.Progress())
        scopeLaunch(
            onBackground = { resetPasswordRepository.resetPassword(email) },
            onUi = { it.handle(uiStateFlux) })
    }

    fun setInitialState() = uiStateFlux.post(ResetPasswordUiState.Initial())
}

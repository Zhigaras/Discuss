package com.zhigaras.login.domain.resetpassword

import androidx.annotation.StringRes
import com.zhigaras.login.presentation.resetpassword.ResetPasswordUiState

interface ResetPasswordResult {

    fun handle(flux: ResetPasswordUiStateFlux.Post)

    object Success : ResetPasswordResult {
        override fun handle(flux: ResetPasswordUiStateFlux.Post) {
            flux.post(ResetPasswordUiState.Success())
        }
    }

    class Error(@StringRes private val errorId: Int) : ResetPasswordResult {
        override fun handle(flux: ResetPasswordUiStateFlux.Post) {
            flux.post(ResetPasswordUiState.SingleEventError(errorId))
        }
    }
}

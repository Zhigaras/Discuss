package com.zhigaras.login.domain.resetpassword

import androidx.annotation.StringRes
import com.zhigaras.login.presentation.resetpassword.ResetPasswordUiState

interface ResetPasswordResult {

    fun handle(communication: ResetPasswordUiStateFlux.Post)

    object Success : ResetPasswordResult {
        override fun handle(communication: ResetPasswordUiStateFlux.Post) {
            communication.post(ResetPasswordUiState.Success())
        }
    }

    class Error(@StringRes private val errorId: Int) : ResetPasswordResult {
        override fun handle(communication: ResetPasswordUiStateFlux.Post) {
            communication.post(ResetPasswordUiState.SingleEventError(errorId))
        }
    }
}

package com.zhigaras.login.domain.resetpassword

import com.zhigaras.core.StateFlux
import com.zhigaras.login.presentation.resetpassword.ResetPasswordUiState

interface ResetPasswordUiStateFlux {
    interface Observe : StateFlux.Observe<ResetPasswordUiState>
    interface Post : StateFlux.Post<ResetPasswordUiState>
    interface Mutable : StateFlux.Mutable<ResetPasswordUiState>, Post, Observe
    class Base : StateFlux.Base<ResetPasswordUiState>(ResetPasswordUiState.Initial()), Mutable
}

package com.zhigaras.login.domain.signup

import com.zhigaras.core.StateFlux
import com.zhigaras.login.presentation.signup.SignUpUiState

interface SignUpUiStateFlux {
    interface Observe : StateFlux.Observe<SignUpUiState>
    interface Post : StateFlux.Post<SignUpUiState>
    interface Mutable : StateFlux.Mutable<SignUpUiState>, Post, Observe
    class Base : StateFlux.Base<SignUpUiState>(SignUpUiState.Initial()), Mutable
}

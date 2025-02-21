package com.zhigaras.login.domain.signin

import com.zhigaras.core.StateFlux
import com.zhigaras.login.presentation.signin.SignInUiState

interface SignInUiStateFlux {
    interface Observe : StateFlux.Observe<SignInUiState>
    interface Post : StateFlux.Post<SignInUiState>
    interface Mutable : StateFlux.Mutable<SignInUiState>, Post, Observe
    class Base : StateFlux.Base<SignInUiState>(SignInUiState.Initial()), Mutable
}

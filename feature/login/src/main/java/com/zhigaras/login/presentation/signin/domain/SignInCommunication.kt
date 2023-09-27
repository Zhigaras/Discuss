package com.zhigaras.login.presentation.signin.domain

import com.zhigaras.core.Communication
import com.zhigaras.login.presentation.signin.SignInUiState

interface SignInCommunication {
    
    interface Observe : Communication.Observe<SignInUiState>
    interface Post : Communication.Post<SignInUiState>
    interface Mutable : Communication.Mutable<SignInUiState>, Post, Observe
    class Base : Communication.Regular<SignInUiState>(), Mutable
}

package com.zhigaras.login.presentation.signup.domain

import com.zhigaras.core.Communication
import com.zhigaras.login.presentation.signup.SignUpUiState

interface SignUpCommunication {
    
    interface Observe : Communication.Observe<SignUpUiState>
    interface Post : Communication.Post<SignUpUiState>
    interface Mutable : Communication.Mutable<SignUpUiState>, Post, Observe
    class Base : Communication.Regular<SignUpUiState>(), Mutable
}

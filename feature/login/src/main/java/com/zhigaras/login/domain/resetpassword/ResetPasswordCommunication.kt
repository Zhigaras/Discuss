package com.zhigaras.login.domain.resetpassword

import com.zhigaras.core.Communication
import com.zhigaras.login.presentation.resetpassword.ResetPasswordUiState

interface ResetPasswordCommunication {
    
    interface Observe : Communication.Observe<ResetPasswordUiState>
    interface Post : Communication.Post<ResetPasswordUiState>
    interface Mutable : Communication.Mutable<ResetPasswordUiState>, Post, Observe
    class Base : Communication.Regular<ResetPasswordUiState>(), Mutable
}

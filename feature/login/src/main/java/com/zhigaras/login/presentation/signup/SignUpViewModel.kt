package com.zhigaras.login.presentation.signup

import com.zhigaras.auth.Auth
import com.zhigaras.core.BaseViewModel
import com.zhigaras.login.domain.SignUpCommunication

class SignUpViewModel(
    communication: SignUpCommunication.Mutable,
    private val auth: Auth
) : BaseViewModel<SignUpUiState>(communication) {
    
    fun signUp(email: String, password: String) {
    
    }
}
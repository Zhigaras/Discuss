package com.zhigaras.login.presentation.signup

import com.zhigaras.auth.Auth
import com.zhigaras.core.BaseViewModel
import com.zhigaras.core.Dispatchers
import com.zhigaras.login.domain.SignUpCommunication

class SignUpViewModel(
    communication: SignUpCommunication.Mutable,
    private val auth: Auth,
    dispatchers: Dispatchers
) : BaseViewModel<SignUpUiState>(communication, dispatchers) {
    
    fun signUp(email: String, password: String) = scopeLaunch(
        onLoading = { communication.post(SignUpUiState.Progress) },
        onSuccess = { communication.post(SignUpUiState.Success) },
        onError = { communication.singleEvent(SignUpUiState.Error(it.errorId())) }
    ) {
        auth.signUpWithEmailAndPassword(email, password)
    }
}
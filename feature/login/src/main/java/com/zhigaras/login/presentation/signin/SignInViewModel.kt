package com.zhigaras.login.presentation.signin

import com.zhigaras.auth.Auth
import com.zhigaras.core.BaseViewModel
import com.zhigaras.core.Dispatchers
import com.zhigaras.login.domain.NavigateToSignUp
import com.zhigaras.login.domain.SignInCommunication

class SignInViewModel(
    private val auth: Auth,
    communication: SignInCommunication.Mutable,
    private val navigateToSignUp: NavigateToSignUp,
    dispatchers: Dispatchers
) : BaseViewModel<SignInUiState>(communication, dispatchers) {
    
    fun signIn(email: String, password: String) = scopeLaunch(
        onLoading = { communication.post(SignInUiState.Progress) },
        onSuccess = { communication.post(SignInUiState.Success) },
        onError = { communication.singleEvent(SignInUiState.Error(it.errorId())) }
    ) {
        auth.signInWithEmailAndPassword(email, password)
    }
    
    fun navigateToSignUp() {
        navigateToSignUp.navigateToSignUp()
    }
}
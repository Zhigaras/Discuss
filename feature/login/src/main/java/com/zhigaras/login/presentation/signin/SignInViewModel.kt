package com.zhigaras.login.presentation.signin

import android.os.Bundle
import com.zhigaras.auth.Auth
import com.zhigaras.core.BaseViewModel
import com.zhigaras.core.Dispatchers
import com.zhigaras.login.databinding.FragmentSignInBinding
import com.zhigaras.login.domain.NavigateToHome
import com.zhigaras.login.domain.NavigateToSignUp
import com.zhigaras.login.domain.SignInCommunication

class SignInViewModel(
    private val auth: Auth,
    private val navigateToSignUp: NavigateToSignUp,
    private val navigateToHome: NavigateToHome,
    communication: SignInCommunication.Mutable,
    dispatchers: Dispatchers
) : BaseViewModel<FragmentSignInBinding, SignInUiState>(communication, dispatchers) {
    
    fun signIn(email: String, password: String) = scopeLaunch(
        onLoading = { communication.post(SignInUiState.Progress) },
        onSuccess = { navigateToHome.navigateToHome() },
        onError = { communication.post(SignInUiState.SingleEventError(it.errorId())) }
    ) {
        auth.signInWithEmailAndPassword(email, password)
    }
    
    fun navigateToSignUp(args: Bundle? = null) {
        navigateToSignUp.navigateToSignUp(args)
    }
}
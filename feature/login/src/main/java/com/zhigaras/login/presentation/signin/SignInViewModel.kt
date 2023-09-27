package com.zhigaras.login.presentation.signin

import android.os.Bundle
import com.zhigaras.core.BaseViewModel
import com.zhigaras.core.Dispatchers
import com.zhigaras.login.databinding.FragmentSignInBinding
import com.zhigaras.login.domain.NavigateToHome
import com.zhigaras.login.domain.NavigateToSignUp
import com.zhigaras.login.presentation.signin.domain.SignInCommunication
import com.zhigaras.login.presentation.signin.domain.SignInRepository

class SignInViewModel(
    private val signInRepository: SignInRepository,
    private val navigateToSignUp: NavigateToSignUp,
    private val navigateToHome: NavigateToHome,
    override val communication: SignInCommunication.Mutable,
    dispatchers: Dispatchers
) : BaseViewModel<FragmentSignInBinding, SignInUiState>(dispatchers),
    NavigateToSignUp {
    
    
    fun signIn(email: String, password: String) {
        communication.postUi(SignInUiState.Progress)
        scopeLaunch({
            signInRepository.signInWithEmailAndPassword(email, password)
        }) {
            it.handle(communication, navigateToHome)
        }
    }
    
    override fun navigateToSignUp(args: Bundle?) {
        navigateToSignUp.navigateToSignUp(args)
    }
}
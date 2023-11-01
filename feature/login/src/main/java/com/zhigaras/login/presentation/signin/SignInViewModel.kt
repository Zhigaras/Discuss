package com.zhigaras.login.presentation.signin

import android.os.Bundle
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.IntentSenderRequest
import com.zhigaras.auth.AuthResultWrapper
import com.zhigaras.auth.OneTapSignInClient
import com.zhigaras.core.BaseViewModel
import com.zhigaras.core.Dispatchers
import com.zhigaras.home.domain.SaveUserToCloud
import com.zhigaras.login.domain.NavigateToHome
import com.zhigaras.login.domain.NavigateToSignUp
import com.zhigaras.login.domain.signin.SignInCommunication
import com.zhigaras.login.domain.signin.SignInRepository

class SignInViewModel(
    private val signInRepository: SignInRepository,
    private val navigateToSignUp: NavigateToSignUp,
    private val saveUserToCloud: SaveUserToCloud,
    private val navigateToHome: NavigateToHome,
    override val communication: SignInCommunication.Mutable,
    dispatchers: Dispatchers
) : BaseViewModel<SignInUiState>(dispatchers),
    NavigateToSignUp {
    
    
    fun signIn(email: String, password: String) {
        communication.postUi(SignInUiState.Progress)
        scopeLaunch({
            signInRepository.signInWithEmailAndPassword(email, password)
        }) {
            it.handle(communication, saveUserToCloud, navigateToHome)
        }
    }
    
    fun handleResult(authResult: AuthResultWrapper, client: OneTapSignInClient) = scopeLaunch({
        signInRepository.handelOneTapSignInResult(authResult, client)
    }) {
        it.handle(communication, saveUserToCloud, navigateToHome)
    }
    
    fun startGoogleSignIn(
        launcher: ActivityResultLauncher<IntentSenderRequest>,
        client: OneTapSignInClient
    ) {
        communication.postUi(SignInUiState.Progress)
        scopeLaunch({ signInRepository.handleOneTapSignInLaunch(launcher, client) }) {
            it.handle(communication, saveUserToCloud, navigateToHome)
        }
    }
    
    override fun navigateToSignUp(args: Bundle?) {
        navigateToSignUp.navigateToSignUp(args)
    }
}
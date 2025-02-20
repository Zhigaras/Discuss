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
import com.zhigaras.login.domain.signin.SignInUiStateFlux
import com.zhigaras.login.domain.signin.SignInRepository

class SignInViewModel(
    private val signInRepository: SignInRepository,
    private val navigateToSignUp: NavigateToSignUp,
    private val saveUserToCloud: SaveUserToCloud,
    private val navigateToHome: NavigateToHome,
    private val uiStateFlux: SignInUiStateFlux.Mutable,
    dispatchers: Dispatchers
) : BaseViewModel<SignInUiState>(dispatchers, uiStateFlux) {

    fun signIn(email: String, password: String) {
        uiStateFlux.post(SignInUiState.Progress())
        scopeLaunch(
            onBackground = { signInRepository.signInWithEmailAndPassword(email, password) },
            onUi = { it.handle(uiStateFlux, saveUserToCloud, navigateToHome) }
        )
    }

    fun handleResult(authResult: AuthResultWrapper, client: OneTapSignInClient) = scopeLaunch(
        onBackground = { signInRepository.handelOneTapSignInResult(authResult, client) },
        onUi = { it.handle(uiStateFlux, saveUserToCloud, navigateToHome) }
    )

    fun startGoogleSignIn(
        launcher: ActivityResultLauncher<IntentSenderRequest>,
        client: OneTapSignInClient
    ) {
        uiStateFlux.post(SignInUiState.Progress())
        scopeLaunch(
            onBackground = { signInRepository.handleOneTapSignInLaunch(launcher, client) },
            onUi = { it.handle(uiStateFlux, saveUserToCloud, navigateToHome) })
    }

    fun navigateToSignUp(args: Bundle?) = safeLaunch {
        navigateToSignUp.navigateToSignUp(args)
    }
}

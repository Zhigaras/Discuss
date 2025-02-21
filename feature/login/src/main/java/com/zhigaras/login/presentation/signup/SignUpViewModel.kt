package com.zhigaras.login.presentation.signup

import com.zhigaras.core.BaseViewModel
import com.zhigaras.core.Dispatchers
import com.zhigaras.home.domain.SaveUserToCloud
import com.zhigaras.login.domain.NavigateToHome
import com.zhigaras.login.domain.signup.SignUpUiStateFlux
import com.zhigaras.login.domain.signup.SignUpRepository

class SignUpViewModel(
    private val signUpRepository: SignUpRepository,
    private val navigateToHome: NavigateToHome,
    private val saveUserToCloud: SaveUserToCloud,
    private val uiStateFlux: SignUpUiStateFlux.Mutable,
    dispatchers: Dispatchers
) : BaseViewModel<SignUpUiState>(dispatchers, uiStateFlux) {

    fun signUp(email: String, password: String) {
        uiStateFlux.post(SignUpUiState.Progress())
        scopeLaunch(
            onBackground = { signUpRepository.signUpWithEmailAndPassword(email, password) },
            onUi = { it.handle(uiStateFlux, navigateToHome, saveUserToCloud) })
    }
}

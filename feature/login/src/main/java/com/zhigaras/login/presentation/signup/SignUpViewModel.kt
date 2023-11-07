package com.zhigaras.login.presentation.signup

import com.zhigaras.core.BaseViewModel
import com.zhigaras.core.Dispatchers
import com.zhigaras.home.domain.SaveUserToCloud
import com.zhigaras.login.domain.NavigateToHome
import com.zhigaras.login.domain.signup.SignUpCommunication
import com.zhigaras.login.domain.signup.SignUpRepository

class SignUpViewModel(
    private val signUpRepository: SignUpRepository,
    private val navigateToHome: NavigateToHome,
    private val saveUserToCloud: SaveUserToCloud,
    override val uiCommunication: SignUpCommunication.Mutable,
    dispatchers: Dispatchers
) : BaseViewModel<SignUpUiState>(dispatchers) {
    
    fun signUp(email: String, password: String) {
        uiCommunication.postUi(SignUpUiState.Progress)
        scopeLaunch({
            signUpRepository.signUpWithEmailAndPassword(email, password)
        }) {
            it.handle(uiCommunication, navigateToHome, saveUserToCloud)
        }
    }
}
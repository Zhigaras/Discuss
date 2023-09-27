package com.zhigaras.login.presentation.signup

import com.zhigaras.core.BaseViewModel
import com.zhigaras.core.Dispatchers
import com.zhigaras.home.domain.SaveUserToCloud
import com.zhigaras.login.databinding.FragmentSignUpBinding
import com.zhigaras.login.domain.NavigateToHome
import com.zhigaras.login.domain.SignUpCommunication
import com.zhigaras.login.domain.SignUpRepository

class SignUpViewModel(
    private val signUpRepository: SignUpRepository,
    private val navigateToHome: NavigateToHome,
    private val saveUserToCloud: SaveUserToCloud,
    override val communication: SignUpCommunication.Mutable,
    dispatchers: Dispatchers
) : BaseViewModel<FragmentSignUpBinding, SignUpUiState>(dispatchers) {
    
    fun signUp(email: String, password: String) = scopeLaunch({
        communication.postUi(SignUpUiState.Progress)
        signUpRepository.signUpWithEmailAndPassword(email, password)
    }) {
        it.handle(communication, navigateToHome, saveUserToCloud)
    }
}
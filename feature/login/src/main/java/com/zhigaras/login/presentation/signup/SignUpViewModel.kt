package com.zhigaras.login.presentation.signup

import com.zhigaras.auth.Auth
import com.zhigaras.core.BaseViewModel
import com.zhigaras.core.Dispatchers
import com.zhigaras.home.domain.SaveUserToCloud
import com.zhigaras.login.databinding.FragmentSignUpBinding
import com.zhigaras.login.domain.NavigateToHome
import com.zhigaras.login.domain.ShowId
import com.zhigaras.login.domain.SignUpCommunication
import com.zhigaras.login.domain.UserMapper

class SignUpViewModel(
    communication: SignUpCommunication.Mutable,
    private val auth: Auth,
    private val navigateToHome: NavigateToHome,
    dispatchers: Dispatchers,
    private val saveUserToCloud: SaveUserToCloud,
    private val userMapper: UserMapper,
    private val showId: ShowId
) : BaseViewModel<FragmentSignUpBinding, SignUpUiState>(communication, dispatchers) {
    
    fun signUp(email: String, password: String) = scopeLaunch(
        onLoading = { communication.post(SignUpUiState.Progress) },
        onSuccess = {
            saveUserToCloud.save(it.map(showId), it.map(userMapper))
            navigateToHome.navigateToHome()
        },
        onError = { communication.post(SignUpUiState.Error(it.errorId())) }
    ) {
        auth.signUpWithEmailAndPassword(email, password)
    }
}
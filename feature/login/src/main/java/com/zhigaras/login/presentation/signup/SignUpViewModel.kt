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
    private val auth: Auth,
    private val navigateToHome: NavigateToHome,
    private val saveUserToCloud: SaveUserToCloud,
    private val userMapper: UserMapper,
    private val showId: ShowId,
    override val communication: SignUpCommunication.Mutable,
    dispatchers: Dispatchers
) : BaseViewModel<FragmentSignUpBinding, SignUpUiState>(dispatchers) {
    
    fun signUp(email: String, password: String) = scopeLaunch(
        onLoading = { communication.postUi(SignUpUiState.Progress) },
        onSuccess = {
            saveUserToCloud.save(it.map(showId), it.map(userMapper))
            navigateToHome.navigateToHome()
        },
        onError = { communication.postUi(SignUpUiState.SingleEventError(it.errorId())) }
    ) {
        auth.signUpWithEmailAndPassword(email, password)
    }
}
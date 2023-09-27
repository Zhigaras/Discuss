package com.zhigaras.login.presentation.signin.domain

import androidx.annotation.StringRes
import com.zhigaras.login.domain.NavigateToHome
import com.zhigaras.login.presentation.signin.SignInUiState

interface SignInResult {
    
    fun handle(
        communication: SignInCommunication.Post,
        navigateToHome: NavigateToHome
    )
    
    object Success : SignInResult {
        
        override fun handle(
            communication: SignInCommunication.Post,
            navigateToHome: NavigateToHome
        ) {
            navigateToHome.navigateToHome()
        }
    }
    
    class Error(@StringRes private val errorId: Int) : SignInResult {
        
        override fun handle(
            communication: SignInCommunication.Post,
            navigateToHome: NavigateToHome
        ) {
            communication.postUi(SignInUiState.SingleEventError(errorId))
        }
    }
}
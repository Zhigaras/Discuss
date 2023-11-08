package com.zhigaras.profile.domain

import androidx.annotation.StringRes
import com.zhigaras.profile.ui.ProfileUiState

interface LogoutResult {
    
    fun handle(communication: ProfileCommunication.Post, navigateToSignIn: NavigateToSignInTwo)
    
    class Success : LogoutResult {
        override fun handle(
            communication: ProfileCommunication.Post,
            navigateToSignIn: NavigateToSignInTwo
        ) {
            communication.postUi(ProfileUiState.Success())
            navigateToSignIn.navigateToSignIn()
        }
    }
    
    class Error(@StringRes private val messageId: Int) : LogoutResult {
        
        override fun handle(
            communication: ProfileCommunication.Post,
            navigateToSignIn: NavigateToSignInTwo
        ) {
            communication.postUi(ProfileUiState.Error(messageId))
        }
    }
}
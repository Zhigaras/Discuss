package com.zhigaras.profile.domain

import androidx.annotation.StringRes
import com.zhigaras.profile.ui.ProfileUiState

interface LogoutResult {
    
    fun handle(communication: ProfileCommunication.Post, navigateToSignIn: NavigateToSignIn)
    
    class Success : LogoutResult {
        override fun handle(
            communication: ProfileCommunication.Post,
            navigateToSignIn: NavigateToSignIn
        ) {
            communication.postUi(ProfileUiState.Success())
            navigateToSignIn.navigateToSignIn()
        }
    }
    
    class Error(@StringRes private val messageId: Int) : LogoutResult {
        
        override fun handle(
            communication: ProfileCommunication.Post,
            navigateToSignIn: NavigateToSignIn
        ) {
            communication.postUi(ProfileUiState.Error(messageId))
        }
    }
}
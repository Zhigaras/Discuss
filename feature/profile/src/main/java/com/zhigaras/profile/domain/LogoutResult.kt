package com.zhigaras.profile.domain

import androidx.annotation.StringRes
import com.zhigaras.profile.ui.ProfileUiState

interface LogoutResult {

    suspend fun handle(communication: ProfileUiStateFlux.Post, navigateToSignIn: NavigateToSignIn)

    class Success : LogoutResult {
        override suspend fun handle(
            communication: ProfileUiStateFlux.Post,
            navigateToSignIn: NavigateToSignIn
        ) {
            communication.post(ProfileUiState.Success())
            navigateToSignIn.navigateToSignIn()
        }
    }

    class Error(@StringRes private val messageId: Int) : LogoutResult {

        override suspend fun handle(
            communication: ProfileUiStateFlux.Post,
            navigateToSignIn: NavigateToSignIn
        ) {
            communication.post(ProfileUiState.Error(messageId))
        }
    }
}

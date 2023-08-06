package com.zhigaras.login.presentation.signin

import androidx.annotation.StringRes
import com.zhigaras.auth.UserDto
import com.zhigaras.core.UiState

interface SignInUiState : UiState {
    
    class Progress : SignInUiState
    
    class Success(private val data: UserDto) : SignInUiState
    
    class Error(@StringRes private val messageId: Int) : SignInUiState
}
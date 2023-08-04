package com.zhigaras.login.presentation.signin

import com.zhigaras.core.presentation.UiState

interface SignInUiState : UiState {
    
    class Progress : SignInUiState
    
    class Success : SignInUiState
    
    class Error : SignInUiState
}
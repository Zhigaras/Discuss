package com.zhigaras.login.presentation.signup

import com.zhigaras.core.UiState

interface SignUpUiState : UiState {
    
    fun update()
    
    class Error()
}
package com.zhigaras.login.presentation.signin

import com.zhigaras.auth.Auth
import com.zhigaras.core.presentation.BaseViewModel
import com.zhigaras.core.presentation.Communication
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class SignInViewModel(
    private val auth: Auth,
    private val communication: Communication.Mutable<SignInUiState> = Communication.Base()
) : BaseViewModel<SignInUiState>(communication) {
    
    fun signIn(email: String, password: String) {
        CoroutineScope(Dispatchers.IO).launch {
            val result = auth.signInWithEmailAndPassword(email, password)
            communication.post(SignInUiState.Success()) // TODO: fix
        }
    }
}
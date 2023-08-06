package com.zhigaras.login.presentation.signin

import android.util.Log
import com.zhigaras.auth.Auth
import com.zhigaras.core.BaseViewModel
import com.zhigaras.login.domain.SignInCommunication
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class SignInViewModel(
    private val auth: Auth,
    communication: SignInCommunication.Mutable
) : BaseViewModel<SignInUiState>(communication) {
    
    fun signIn(email: String, password: String) {
        CoroutineScope(Dispatchers.IO).launch {
            val result = auth.signInWithEmailAndPassword(email, password)
            communication.post(SignInUiState.Success()) // TODO: fix
            Log.d("AAA from viewModel", result.toString())
        }
    }
}
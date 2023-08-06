package com.zhigaras.login.presentation.signin

import android.util.Log
import androidx.lifecycle.viewModelScope
import com.zhigaras.auth.Auth
import com.zhigaras.core.BaseViewModel
import com.zhigaras.login.domain.NavigateToSignUp
import com.zhigaras.login.domain.SignInCommunication
import kotlinx.coroutines.launch

class SignInViewModel(
    private val auth: Auth,
    communication: SignInCommunication.Mutable,
    private val navigateToSignUp: NavigateToSignUp
) : BaseViewModel<SignInUiState>(communication) {
    
    fun signIn(email: String, password: String) {
        viewModelScope.launch {
            val result = auth.signInWithEmailAndPassword(email, password)
            communication.post(SignInUiState.Success()) // TODO: fix
            Log.d("AAA from viewModel", result.toString())
        }
    }
    
    fun navigateToSignUp() {
        navigateToSignUp.navigateToSignUp()
    }
}
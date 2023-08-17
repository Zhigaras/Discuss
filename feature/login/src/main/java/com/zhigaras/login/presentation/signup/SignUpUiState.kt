package com.zhigaras.login.presentation.signup

import android.view.View
import androidx.annotation.StringRes
import com.google.android.material.snackbar.Snackbar
import com.zhigaras.core.UiState
import com.zhigaras.login.databinding.FragmentSignUpBinding

interface SignUpUiState : UiState<FragmentSignUpBinding> {
    
    object Progress : SignUpUiState {
        override fun update(binding: FragmentSignUpBinding) {
            binding.progressLayout.root.visibility = View.VISIBLE
        }
    }
    
    object Success : SignUpUiState {
        override fun update(binding: FragmentSignUpBinding) {
            binding.progressLayout.root.visibility = View.GONE
        }
    }
    
    class Error(@StringRes val messageId: Int) : SignUpUiState, UiState.SingleEvent<FragmentSignUpBinding>() {
        override val block: (FragmentSignUpBinding) -> Unit = {
            Snackbar.make(it.root, messageId, Snackbar.LENGTH_INDEFINITE).show()
        }
    }
}
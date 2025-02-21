package com.zhigaras.login.presentation.signup

import android.view.View
import android.widget.Toast
import androidx.annotation.StringRes
import com.zhigaras.core.UiState
import com.zhigaras.login.databinding.FragmentSignUpBinding

interface SignUpUiState : UiState<FragmentSignUpBinding> {

    class Initial : SignUpUiState {
        override fun update(binding: FragmentSignUpBinding) = Unit
    }

    class Progress : SignUpUiState {
        override fun update(binding: FragmentSignUpBinding) {
            binding.progressLayout.root.visibility = View.VISIBLE
        }
    }

    class SingleEventError(@StringRes val messageId: Int) : SignUpUiState,
        UiState.SingleEvent<FragmentSignUpBinding>() {
        override val block: FragmentSignUpBinding.() -> Unit = {
            progressLayout.root.visibility = View.GONE
            Toast.makeText(root.context, messageId, Toast.LENGTH_LONG).show()
        }
    }
}

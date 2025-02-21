package com.zhigaras.login.presentation.resetpassword

import android.view.View
import androidx.annotation.StringRes
import com.zhigaras.core.UiState
import com.zhigaras.login.databinding.DialogResetPasswordBinding

interface ResetPasswordUiState : UiState<DialogResetPasswordBinding> {
    class Initial : ResetPasswordUiState {
        override fun update(binding: DialogResetPasswordBinding) {
            binding.progressBar.visibility = View.GONE
            binding.resetLayout.visibility = View.VISIBLE
            binding.successLayout.visibility = View.GONE
        }
    }

    class Progress : ResetPasswordUiState {
        override fun update(binding: DialogResetPasswordBinding) {
            binding.resetPasswordButton.visibility = View.GONE
            binding.progressBar.visibility = View.VISIBLE
        }
    }

    class Success : ResetPasswordUiState {
        override fun update(binding: DialogResetPasswordBinding) {
            binding.progressBar.visibility = View.GONE
            binding.resetLayout.visibility = View.GONE
            binding.successLayout.visibility = View.VISIBLE
        }
    }

    class SingleEventError(@StringRes val messageId: Int) : ResetPasswordUiState,
        UiState.SingleEvent<DialogResetPasswordBinding>() {

        override val block: DialogResetPasswordBinding.() -> Unit = {
            emailInput.root.setError(messageId)
            progressBar.visibility = View.GONE
        }
    }
}

package com.zhigaras.login.presentation.signin

import android.content.Intent
import android.view.View
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.annotation.StringRes
import com.google.android.material.snackbar.Snackbar
import com.zhigaras.auth.OneTapSignInClient
import com.zhigaras.core.UiState
import com.zhigaras.login.R
import com.zhigaras.login.databinding.FragmentSignInBinding

interface SignInUiState : UiState<FragmentSignInBinding> {
    
    object Progress : SignInUiState {
        
        override fun update(binding: FragmentSignInBinding) {
            binding.progressLayout.root.visibility = View.VISIBLE
        }
    }
    
    class StartAuth(private val launcher: ActivityResultLauncher<Intent>) : SignInUiState {
        
        override fun update(binding: FragmentSignInBinding) {
            launcher.launch(OneTapSignInClient().getIntent(binding.root.context))
        }
    }
    
    class SingleEventError(@StringRes val messageId: Int) : SignInUiState,
        UiState.SingleEvent<FragmentSignInBinding>() {
        override val block: FragmentSignInBinding.() -> Unit = {
            progressLayout.root.visibility = View.GONE
            Toast.makeText(root.context, messageId, Toast.LENGTH_LONG).show()
        }
    }
    
    class PersistentError(@StringRes val messageId: Int) : SignInUiState {
        override fun update(binding: FragmentSignInBinding) {
            binding.progressLayout.root.visibility = View.GONE
            Snackbar.make(binding.root, messageId, Snackbar.LENGTH_INDEFINITE).also { snackbar ->
                snackbar.setAction(binding.root.context.getString(R.string.close)) {
                    snackbar.dismiss()
                }
                snackbar.show()
            }
        }
    }
}
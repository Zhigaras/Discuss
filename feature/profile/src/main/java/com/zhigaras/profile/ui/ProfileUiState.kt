package com.zhigaras.profile.ui

import android.widget.Toast
import androidx.annotation.StringRes
import com.zhigaras.core.UiState
import com.zhigaras.profile.databinding.FragmentProfileBinding

interface ProfileUiState : UiState<FragmentProfileBinding> {
    
    class Progress : ProfileUiState {
        
        override fun update(binding: FragmentProfileBinding) {
            binding.logoutButton.isEnabled = false
        }
    }
    
    class Success : ProfileUiState {
        
        override fun update(binding: FragmentProfileBinding) = Unit
    }
    
    class Error(@StringRes private val messageId: Int) : ProfileUiState,
        UiState.SingleEvent<FragmentProfileBinding>() {
        
        override val block: FragmentProfileBinding.() -> Unit = {
            Toast.makeText(root.context, messageId, Toast.LENGTH_SHORT).show()
            logoutButton.isEnabled = true
        }
    }
}
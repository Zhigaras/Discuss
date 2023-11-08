package com.zhigaras.profile.ui

import android.view.View
import android.widget.Toast
import androidx.annotation.StringRes
import com.zhigaras.core.UiState
import com.zhigaras.profile.databinding.FragmentProfileBinding

interface ProfileUiState : UiState<FragmentProfileBinding> {
    
    class Progress : ProfileUiState {
        
        override fun update(binding: FragmentProfileBinding) {
            binding.progressLayout.root.visibility = View.VISIBLE
        }
    }
    
    class Success : ProfileUiState {
        
        override fun update(binding: FragmentProfileBinding) {
            binding.progressLayout.root.visibility = View.GONE
        }
    }
    
    class Error(@StringRes private val messageId: Int) : ProfileUiState,
        UiState.SingleEvent<FragmentProfileBinding>() {
        
        override val block: FragmentProfileBinding.() -> Unit = {
            Toast.makeText(root.context, messageId, Toast.LENGTH_SHORT).show()
            progressLayout.root.visibility = View.GONE
        }
    }
}
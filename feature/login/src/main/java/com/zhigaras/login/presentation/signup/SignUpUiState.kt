package com.zhigaras.login.presentation.signup

import android.view.View
import android.widget.FrameLayout
import android.widget.Toast
import androidx.annotation.StringRes
import com.zhigaras.core.UiState

interface SignUpUiState : UiState {
    
    fun update(progressLayout: FrameLayout)
    
    object Progress : SignUpUiState {
        override fun update(progressLayout: FrameLayout) {
            progressLayout.visibility = View.VISIBLE
        }
    }
    
    object Success : SignUpUiState {
        override fun update(progressLayout: FrameLayout) {
            progressLayout.visibility = View.GONE
        }
    }
    
    class Error(@StringRes val messageId: Int) : SignUpUiState {
        override fun update(progressLayout: FrameLayout) {
            val context = progressLayout.context
            Toast.makeText(context, context.getString(messageId), Toast.LENGTH_LONG).show()
            progressLayout.visibility = View.GONE
        }
    }
}
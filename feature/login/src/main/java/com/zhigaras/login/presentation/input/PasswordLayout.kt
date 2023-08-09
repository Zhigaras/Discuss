package com.zhigaras.login.presentation.input

import android.content.Context
import android.text.TextWatcher
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import androidx.core.view.isVisible
import com.zhigaras.login.databinding.PasswordLayoutBinding

class PasswordLayout @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet,
    defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr), InputValidation {
    
    private val binding = PasswordLayoutBinding.inflate(LayoutInflater.from(context))
    
    private val passwordLayout = binding.inputPassword.root
    private val confirmPasswordLayout = binding.confirmPassword.root
    private val errorView = binding.passwordMismatchErrorView
    private val passwordList = listOf(passwordLayout, confirmPasswordLayout)
    
    init {
        addView(binding.root)
    }
    
    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        addTextWatcher(AuthTextWatcher { errorView.visibility = INVISIBLE })
    }
    
    override fun isValid(): Boolean {
        return innerIsValid()
    }
    
    override fun addTextWatcher(textWatcher: TextWatcher) {
        passwordList.forEach { it.addTextWatcher(textWatcher) }
    }
    
    private fun innerIsValid(): Boolean {
        val isPasswordsEquals = passwordLayout.text() == confirmPasswordLayout.text()
        errorView.isVisible = !isPasswordsEquals
        val passListValidation = passwordList.map { it.isValid() }
        return passListValidation.all { it } && isPasswordsEquals
    }
    
    fun text(): String {
        return passwordLayout.text()
    }
}
package com.zhigaras.login.presentation.input

import android.content.Context
import android.text.TextWatcher
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import com.zhigaras.login.R
import com.zhigaras.login.databinding.PasswordLayoutBinding

class PasswordLayout @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet,
    defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr), InputValidation {
    
    private val binding = PasswordLayoutBinding.inflate(LayoutInflater.from(context))
    private val errorTextId = R.string.password_matching_error
    private val passwordLayout = binding.inputPassword.root
    private val confirmPasswordLayout = binding.confirmPassword.root
    private val errorView = binding.passwordMismatchErrorView
    private val passwordList = listOf(passwordLayout, confirmPasswordLayout)
    
    init {
        addView(binding.root)
    }
    
    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        addTextWatcher(AuthTextWatcher { errorView.text = "" })
    }
    
    override fun isValid(): Boolean {
        val isPasswordsEquals = passwordLayout.text() == confirmPasswordLayout.text()
        errorView.text = if (isPasswordsEquals) "" else context.getText(errorTextId)
        val passListValidation = passwordList.map { it.isValid() }
        return passListValidation.all { it } && isPasswordsEquals
    }
    
    override fun addTextWatcher(textWatcher: TextWatcher) {
        passwordList.forEach { it.addTextWatcher(textWatcher) }
    }
    
    fun text(): String {
        return passwordLayout.text()
    }
}
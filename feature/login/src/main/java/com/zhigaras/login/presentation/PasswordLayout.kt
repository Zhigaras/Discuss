package com.zhigaras.login.presentation

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import com.zhigaras.login.R
import com.zhigaras.login.databinding.PasswordLayoutBinding

class PasswordLayout @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet,
    defStyleAttr: Int = 0
) : CustomInputLayout(context, attrs, defStyleAttr) {
    
    private val binding: PasswordLayoutBinding
    
    init {
        binding = PasswordLayoutBinding.inflate(LayoutInflater.from(context), this, true)
    }
    
    override val errorMessageId: Int = R.string.password_matching_error
    
    override fun isValid(): Boolean {
        val isValid = innerIsValid()
        binding.passwordMismatchErrorView.text =
            if (isValid) "" else context.getString(errorMessageId)
        return isValid
    }
    
    override fun innerIsValid(): Boolean {
        with(binding) {
            passwordLayout.isValid()
            confirmPasswordLayout.isValid()
            return passwordLayout.text() == confirmPasswordLayout.text()
        }
    }
    
    override fun text(): String {
        return binding.passwordLayout.text()
    }
}
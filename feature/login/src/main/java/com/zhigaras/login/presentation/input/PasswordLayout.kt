package com.zhigaras.login.presentation.input

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.LinearLayout
import com.zhigaras.login.R
import com.zhigaras.login.databinding.PasswordLayoutBinding

class PasswordLayout @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet,
    defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr), InputValidation {
    
    private val binding: PasswordLayoutBinding
    
    init {
        binding = PasswordLayoutBinding.inflate(LayoutInflater.from(context), this)
        listOf(binding.passwordEdittext, binding.confirmPasswordEdittext).forEach {
            it.addTextChangedListener(AuthTextWatcher {
                binding.passwordMismatchErrorView.visibility = GONE
            })
        }
    }
    
    private val errorMessageId: Int = R.string.password_matching_error
    
    override fun isValid(): Boolean {
        val isValid = innerIsValid()
        binding.passwordMismatchErrorView.text =
            if (isValid) "" else context.getString(errorMessageId)
        return isValid
    }
    
    private fun innerIsValid(): Boolean {
        with(binding) {
            passwordLayout.isValid()
            confirmPasswordLayout.isValid()
            return passwordLayout.text() == confirmPasswordLayout.text()
        }
    }
    
    fun text(): String {
        return binding.passwordLayout.text()
    }
}
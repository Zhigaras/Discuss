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
    
    private val binding = PasswordLayoutBinding.inflate(LayoutInflater.from(context), this)
    private val passwordLayout = // TODO: refactor
        binding.passwordLayout.findViewById<AbstractInputLayout>(R.id.password_layout)
    private val confirmPasswordLayout =
        binding.confirmPasswordLayout.findViewById<AbstractInputLayout>(R.id.password_layout)
    
    init {
        orientation = VERTICAL
        listOf(passwordLayout.editText, confirmPasswordLayout.editText).forEach {
            it?.addTextChangedListener(AuthTextWatcher {
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
        passwordLayout.isValid()
        confirmPasswordLayout.isValid()
        return passwordLayout.text() == confirmPasswordLayout.text()
    }
    
    
    fun text(): String {
        return passwordLayout.text()
    }
}
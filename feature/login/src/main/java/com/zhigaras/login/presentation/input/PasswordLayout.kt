package com.zhigaras.login.presentation.input

import android.content.Context
import android.text.TextWatcher
import android.util.AttributeSet
import android.widget.LinearLayout
import android.widget.TextView
import com.zhigaras.login.R

class PasswordLayout @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet,
    defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr), InputValidation, Init {
    
    private val passwordLayout = findViewById<PasswordInput>(R.id.input_password)
    private val confirmPasswordLayout = findViewById<PasswordInput>(R.id.confirm_password)
    private val errorView = findViewById<TextView>(R.id.password_mismatch_error_view)
    
    init {
        initTextWatcher(AuthTextWatcher { errorView.visibility = GONE })
    }
    
    private val errorMessageId: Int = R.string.password_matching_error
    
    override fun isValid(): Boolean {
        val isValid = innerIsValid()
        errorView.text = if (isValid) "" else context.getString(errorMessageId)
        return isValid
    }
    
    override fun initTextWatcher(textWatcher: TextWatcher) {
        listOf(passwordLayout, confirmPasswordLayout).forEach {
            it?.initTextWatcher(textWatcher)
        }
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
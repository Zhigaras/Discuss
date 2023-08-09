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
) : LinearLayout(context, attrs, defStyleAttr), InputValidation {
    
    private val errorTextId = R.string.password_matching_error
    private lateinit var errorView: TextView
    private lateinit var passwordList: List<PasswordInput>
    
    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        orientation = VERTICAL
        errorView = findViewById(R.id.password_mismatch_error_view)
        passwordList = listOf(
            findViewById(R.id.input_password),
            findViewById(R.id.confirm_password)
        )
        addTextWatcher(AuthTextWatcher { errorView.text = "" })
    }
    
    override fun isValid(): Boolean {
        val isPasswordsEquals = passwordList.first().text() == passwordList.last().text()
        errorView.text = if (isPasswordsEquals) "" else context.getText(errorTextId)
        val passListValidation = passwordList.map { it.isValid() }
        return passListValidation.all { it } && isPasswordsEquals
    }
    
    override fun addTextWatcher(textWatcher: TextWatcher) {
        passwordList.forEach { it.addTextWatcher(textWatcher) }
    }
    
    override fun text(): String {
        return passwordList.first().text()
    }
}
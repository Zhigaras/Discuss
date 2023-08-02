package com.zhigaras.login.presentation.input

import android.content.Context
import android.util.AttributeSet
import com.google.android.material.textfield.TextInputLayout

abstract class AbstractInputLayout @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet,
    defStyleAttr: Int = 0
) : TextInputLayout(context, attrs, defStyleAttr), InputValidation {
    
    protected abstract val errorMessageId: Int
    private val textWatcher = AuthTextWatcher { error = "" }
    fun text(): String = (editText?.text ?: "").toString()
    
    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        editText?.addTextChangedListener(textWatcher)
    }
    
    override fun isValid(): Boolean {
        val isValid = innerIsValid()
        error = if (isValid) "" else context.getString(errorMessageId)
        return isValid
    }
    
    protected abstract fun innerIsValid(): Boolean
}

interface InputValidation {
    
    fun isValid(): Boolean
}
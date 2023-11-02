package com.zhigaras.login.presentation.input

import android.content.Context
import android.util.AttributeSet
import android.util.Patterns
import com.zhigaras.core.AbstractInputLayout
import com.zhigaras.login.R

class EmailInput @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : AbstractInputLayout(context, attrs, defStyleAttr) {
    
    override val errorMessageId = R.string.email_input_error
    
    init {
        endIconMode = END_ICON_CLEAR_TEXT
    }
    
    override fun innerIsValid(): Boolean {
        return Patterns.EMAIL_ADDRESS.matcher(text()).matches()
    }
}
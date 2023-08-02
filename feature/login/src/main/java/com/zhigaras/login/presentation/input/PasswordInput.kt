package com.zhigaras.login.presentation.input

import android.content.Context
import android.util.AttributeSet
import com.zhigaras.login.R

class PasswordInput @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet,
    defStyleAttr: Int = 0
) : AbstractInputLayout(context, attrs, defStyleAttr) {
    
    override val errorMessageId: Int = R.string.password_input_error
    
    override fun innerIsValid(): Boolean {
        return text().length > 5
    }
}
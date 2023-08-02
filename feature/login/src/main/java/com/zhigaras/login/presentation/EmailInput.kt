package com.zhigaras.login.presentation

import android.content.Context
import android.util.AttributeSet
import com.zhigaras.login.R

class EmailInput @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet,
    defStyleAttr: Int = 0
) : CustomInputLayout(context, attrs, defStyleAttr) {
    
    override val errorMessageId = R.string.email_input_error
    
    override fun innerIsValid(): Boolean {
        return text().length > 1
    }
}
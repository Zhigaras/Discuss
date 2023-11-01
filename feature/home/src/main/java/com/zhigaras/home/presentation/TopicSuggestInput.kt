package com.zhigaras.home.presentation

import android.content.Context
import android.util.AttributeSet
import com.zhigaras.core.AbstractInputLayout
import com.zhigaras.home.R

class TopicSuggestInput @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : AbstractInputLayout(context, attrs, defStyleAttr) {
    
    override val errorMessageId = R.string.topic_suggest_error_message
    
    override fun innerIsValid(): Boolean {
        return text().length < SUBJECT_MAX_LENGTH
    }
    
    override fun isValid(): Boolean {
        val isValid = innerIsValid()
        error = if (isValid) "" else context.getString(errorMessageId, SUBJECT_MAX_LENGTH)
        return isValid
    }
    
    companion object {
        private const val SUBJECT_MAX_LENGTH = 100
    }
}
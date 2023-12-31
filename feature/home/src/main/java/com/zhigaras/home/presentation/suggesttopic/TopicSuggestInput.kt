package com.zhigaras.home.presentation.suggesttopic

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
        return text().length < TOPIC_MAX_LENGTH && text().trim().isNotBlank()
    }
    
    override fun isValid(): Boolean {
        val isValid = innerIsValid()
        error = if (isValid) "" else context.getString(errorMessageId, TOPIC_MAX_LENGTH)
        return isValid
    }
    
    companion object {
        private const val TOPIC_MAX_LENGTH = 100
    }
}
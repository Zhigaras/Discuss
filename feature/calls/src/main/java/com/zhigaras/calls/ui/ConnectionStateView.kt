package com.zhigaras.calls.ui

import android.content.Context
import android.util.AttributeSet
import androidx.annotation.StringRes
import androidx.appcompat.widget.AppCompatTextView

class ConnectionStateView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : AppCompatTextView(context, attrs, defStyleAttr) {
    
    fun show(@StringRes messageId: Int) {
        visibility = VISIBLE
        setText(messageId)
    }
    
    fun hide() {
        visibility = GONE
    }
}
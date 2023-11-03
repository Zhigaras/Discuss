package com.zhigaras.discuss.presentation

import android.content.Context
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatTextView
import com.zhigaras.discuss.R

class ConnectionStateView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : AppCompatTextView(context, attrs, defStyleAttr) {
    
    fun showConnectionLost() {
        visibility = VISIBLE
        setBackgroundColor(context.getColor(com.zhigaras.messaging.R.color.dark_red))
        setText(R.string.connection_lost)
    }
    
    fun hideConnectionLost() {
        setBackgroundColor(context.getColor(com.zhigaras.messaging.R.color.light_green))
        setText(R.string.connection_restored)
        visibility = GONE
    }
}
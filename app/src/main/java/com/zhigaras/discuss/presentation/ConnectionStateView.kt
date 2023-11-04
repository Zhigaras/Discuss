package com.zhigaras.discuss.presentation

import android.content.Context
import android.graphics.drawable.TransitionDrawable
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatTextView
import androidx.lifecycle.findViewTreeLifecycleOwner
import androidx.lifecycle.lifecycleScope
import com.zhigaras.discuss.R
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class ConnectionStateView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : AppCompatTextView(context, attrs, defStyleAttr) {
    
    private lateinit var backgroundAnimation: TransitionDrawable
    
    fun showConnectionLost() {
        setBackgroundResource(R.drawable.connection_lost_bg)
        visibility = VISIBLE
        setText(R.string.connection_lost)
    }
    
    fun hideConnectionLost() {
        setBackgroundResource(R.drawable.connection_state_bg)
        setText(R.string.connection_restored)
        backgroundAnimation = background as TransitionDrawable
        backgroundAnimation.startTransition(300)
        findViewTreeLifecycleOwner()?.lifecycleScope?.launch {
            delay(1500)
            visibility = GONE
        }
    }
}
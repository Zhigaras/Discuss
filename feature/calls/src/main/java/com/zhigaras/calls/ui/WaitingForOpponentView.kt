package com.zhigaras.calls.ui

import android.content.Context
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatTextView
import com.zhigaras.webrtc.R
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.cancelChildren
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch

class WaitingForOpponentView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : AppCompatTextView(context, attrs, defStyleAttr) {
    
    private val scope = CoroutineScope(Dispatchers.Main)
    private var counter = 1
    private var inProgress = false
    private var reason = ""
    
    fun startSearch() {
        visibility = VISIBLE
        reason = context.getString(R.string.search)
        startProgress()
    }
    
    fun startWaiting() {
        visibility = VISIBLE
        reason = context.getString(R.string.waiting)
    }
    
    fun startConnecting() {
        reason = context.getString(R.string.connecting)
    }
    
    private fun startProgress() {
        if (inProgress) return
        inProgress = true
        scope.launch {
            while (isActive) {
                text = buildString {
                    append(reason)
                    repeat(counter) { append('.') }
                    repeat(3 - counter) { append(' ') }
                }
                delay(300)
                if (++counter == 4) counter = 0
            }
        }
    }
    
    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        scope.cancel()
    }
    
    fun stop() {
        visibility = GONE
        scope.coroutineContext.cancelChildren()
        inProgress = false
    }
}
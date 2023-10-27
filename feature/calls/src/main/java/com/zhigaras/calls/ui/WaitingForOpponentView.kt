package com.zhigaras.calls.ui

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import androidx.appcompat.widget.AppCompatTextView
import com.zhigaras.webrtc.R
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class WaitingForOpponentView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : AppCompatTextView(context, attrs, defStyleAttr) {
    
    private lateinit var scope: CoroutineScope
    private var counter = 1
    private var inProgress = false
    private var reason = ""
    
    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        Log.d("AAASS", "attached")
        scope = CoroutineScope(Dispatchers.Main)
    }
    
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
            while (inProgress) {
                Log.d("AAASS", counter.toString())
                text = buildString {
                    append(reason)
                    repeat(counter) { append('.') }
                }
                delay(300)
                if (++counter == 4) counter = 0
            }
        }
    }
    
    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        Log.d("AAASS", "detached")
        scope.cancel()
    }
    
    fun stop() {
        visibility = GONE
        inProgress = false
    }
}
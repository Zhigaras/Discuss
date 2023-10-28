package com.zhigaras.calls.ui

import android.content.Context
import android.os.Bundle
import android.os.Parcelable
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
    
    init {
        onResume()
    }
    
    fun startSearch() {
        reason = context.getString(R.string.search)
        startProgress()
    }
    
    fun startWaiting() {
        reason = context.getString(R.string.waiting)
        startProgress()
    }
    
    fun startConnecting() {
        reason = context.getString(R.string.connecting)
        startProgress()
    }
    
    fun startReconnect() {
        reason = context.getString(R.string.trying_to_reconnect)
        startProgress()
    }
    
    private fun startProgress() {
        inProgress = true
        visibility = VISIBLE
        scope.coroutineContext.cancelChildren()
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
    
    override fun onSaveInstanceState(): Parcelable {
        val bundle = Bundle().also {
            it.putParcelable(STATE_KEY, super.onSaveInstanceState())
            it.putBoolean(PROGRESS_KEY, inProgress)
            it.putString(REASON_KEY, reason)
        }
        return bundle
    }
    
    override fun onRestoreInstanceState(state: Parcelable?) {
        if (state is Bundle) {
            inProgress = state.getBoolean(PROGRESS_KEY, false)
            reason = state.getString(REASON_KEY, "")
            super.onRestoreInstanceState(state.getParcelable(STATE_KEY))
        } else {
            super.onRestoreInstanceState(state)
        }
    }
    
    fun onPause() {
        scope.coroutineContext.cancelChildren()
    }
    
    fun onResume() {
        if (inProgress) startProgress()
    }
    
    fun stop() {
        visibility = GONE
        scope.coroutineContext.cancelChildren()
        inProgress = false
    }
    
    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        scope.cancel()
    }
    
    companion object {
        private const val PROGRESS_KEY = "inProgress"
        private const val REASON_KEY = "reason"
        private const val STATE_KEY = "state"
    }
}
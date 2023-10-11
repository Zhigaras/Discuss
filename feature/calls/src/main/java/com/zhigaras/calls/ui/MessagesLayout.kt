package com.zhigaras.calls.ui

import android.animation.LayoutTransition
import android.content.Context
import android.util.AttributeSet
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.core.view.isVisible
import androidx.lifecycle.findViewTreeLifecycleOwner
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.RecyclerView
import com.zhigaras.webrtc.R
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class MessagesLayout @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr) {
    
    private lateinit var showHideButton: ImageView
    private lateinit var messagesRv: RecyclerView
    private var isExpanded = MutableStateFlow(false)
    
    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        layoutTransition = LayoutTransition()
        showHideButton = findViewById(R.id.show_hide_button)
        messagesRv = findViewById(R.id.messages_rv)
        findViewTreeLifecycleOwner()?.lifecycleScope?.launch {
            isExpanded.collect {
                showHideButton.setImageResource(
                    if (isExpanded.value) R.drawable.baseline_expand_less_24
                    else R.drawable.baseline_expand_more_24
                )
                messagesRv.isVisible = it
            }
        }
        showHideButton.setOnClickListener {
            isExpanded.value = !isExpanded.value
        }
    }
}
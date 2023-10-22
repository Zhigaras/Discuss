package com.zhigaras.messaging.ui

import android.animation.LayoutTransition
import android.content.Context
import android.util.AttributeSet
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.core.view.isVisible
import androidx.lifecycle.findViewTreeLifecycleOwner
import androidx.lifecycle.lifecycleScope
import com.zhigaras.messaging.R
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class PortraitMessagesLayout @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : LandscapeMessagesLayout(context, attrs, defStyleAttr) {
    
    private val showHideButton: ImageView by lazy { findViewById(R.id.show_hide_button) }
    private val layoutToHide: LinearLayout by lazy { findViewById(R.id.layout_to_hide) }
    
    private var isExpanded = MutableStateFlow(false)
    
    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val maxHeightMeasureSpec = MeasureSpec.makeMeasureSpec(800, MeasureSpec.AT_MOST)
        super.onMeasure(widthMeasureSpec, maxHeightMeasureSpec)
    }
    
    override fun onAttachedToWindow() {
        layoutTransition = LayoutTransition()
        findViewTreeLifecycleOwner()?.lifecycleScope?.launch {
            isExpanded.collect {
                showHideButton.setImageResource(
                    if (isExpanded.value) R.drawable.baseline_expand_less_24
                    else R.drawable.baseline_expand_more_24
                )
                layoutToHide.isVisible = it
            }
        }
        showHideButton.setOnClickListener {
            isExpanded.value = !isExpanded.value
        }
        super.onAttachedToWindow()
    }
}
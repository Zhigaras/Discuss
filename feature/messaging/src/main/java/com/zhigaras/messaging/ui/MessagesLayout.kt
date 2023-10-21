package com.zhigaras.messaging.ui

import android.animation.LayoutTransition
import android.content.Context
import android.util.AttributeSet
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.appcompat.widget.AppCompatEditText
import androidx.core.view.isVisible
import androidx.lifecycle.findViewTreeLifecycleOwner
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.RecyclerView
import com.zhigaras.adapterdelegate.CompositeAdapter
import com.zhigaras.messaging.R
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import org.koin.java.KoinJavaComponent.inject

class MessagesLayout @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr) {
    
    private val showHideButton: ImageView by lazy { findViewById(R.id.show_hide_button) }
    private val messagesRv: RecyclerView by lazy { findViewById(R.id.messages_rv) }
    private val editText: AppCompatEditText by lazy { findViewById(R.id.new_message_edit_text) }
    private val sendButton: ImageView by lazy { findViewById(R.id.send_message_button) }
    
    private var isExpanded = MutableStateFlow(false)
    private val viewModel: MessagesViewModel by inject(MessagesViewModel::class.java)
    
    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val maxHeightMeasureSpec = MeasureSpec.makeMeasureSpec(800, MeasureSpec.AT_MOST)
        super.onMeasure(widthMeasureSpec, maxHeightMeasureSpec)
    }
    
    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        layoutTransition = LayoutTransition()
        findViewTreeLifecycleOwner()?.lifecycleScope?.launch {
            isExpanded.collect {
                showHideButton.setImageResource(
                    if (isExpanded.value) R.drawable.baseline_expand_less_24
                    else R.drawable.baseline_expand_more_24
                )
                changeVisibility(it)
            }
        }
        showHideButton.setOnClickListener {
            isExpanded.value = !isExpanded.value
        }
        val adapter = CompositeAdapter.Builder()
            .addAdapter(IncomingMessageAdapter())
            .addAdapter(OutgoingMessageAdapter())
            .build()
        messagesRv.adapter = adapter
        viewModel.observe(findViewTreeLifecycleOwner() ?: return) {
            it.handle(adapter)
        }
        sendButton.setOnClickListener {
            val msg = editText.text
            if (msg.isNullOrBlank()) return@setOnClickListener
            else {
                viewModel.sendMessage(msg.trim().toString())
                editText.text = null
            }
        }
    }
    
    private fun changeVisibility(isVisible: Boolean) {
        messagesRv.isVisible = isVisible
        editText.isVisible = isVisible
        sendButton.isVisible = isVisible
    }
}
package com.zhigaras.calls.ui

import android.animation.LayoutTransition
import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.core.view.isVisible
import androidx.lifecycle.findViewTreeLifecycleOwner
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.RecyclerView
import com.zhigaras.webrtc.R
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
    private val messageInput: EditText by lazy { findViewById(R.id.new_message_edit_text) }
    private val sendButton: Button by lazy { findViewById(R.id.send_message_button) }
    private var isExpanded = MutableStateFlow(false)
    private val viewModel: MessagesViewModel by inject(MessagesViewModel::class.java)
    
    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        layoutTransition = LayoutTransition()
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
        val adapter = MessagesAdapter()
        messagesRv.adapter = adapter
        viewModel.observeMessages(findViewTreeLifecycleOwner() ?: return) {
            Log.d("QQQ message received", it)
//            adapter.setData(it)
        }
        sendButton.setOnClickListener {
            Log.d("QQQ message sent", messageInput.text.toString())
            viewModel.sendMessage(messageInput.text.toString())
        }
    }
}
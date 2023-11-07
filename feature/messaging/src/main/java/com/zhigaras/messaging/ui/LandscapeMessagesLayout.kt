package com.zhigaras.messaging.ui

import android.content.Context
import android.util.AttributeSet
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.appcompat.widget.AppCompatEditText
import androidx.core.view.isVisible
import androidx.lifecycle.findViewTreeLifecycleOwner
import androidx.recyclerview.widget.RecyclerView
import com.zhigaras.adapterdelegate.CompositeAdapter
import com.zhigaras.core.viewModel
import com.zhigaras.messaging.R

open class LandscapeMessagesLayout @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr) {
    
    private val messagesRv: RecyclerView by lazy { findViewById(R.id.messages_rv) }
    private val editText: AppCompatEditText by lazy { findViewById(R.id.new_message_edit_text) }
    private val sendButton: ImageView by lazy { findViewById(R.id.send_message_button) }
    
    private val viewModel: MessagesViewModel by viewModel()
    
    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        val adapter = CompositeAdapter.Builder()
            .addAdapter(IncomingMessageAdapter())
            .addAdapter(OutgoingMessageAdapter())
            .build()
        messagesRv.adapter = adapter
        viewModel.observe(findViewTreeLifecycleOwner() ?: return) {// TODO: fix return!!!
            it.handle(adapter)
        }
        val textWatcher = MessageTextWatcher {
            sendButton.isVisible = it.isNotBlank()
        }
        editText.addTextChangedListener(textWatcher)
        sendButton.setOnClickListener {
            val msg = editText.text
            if (msg.isNullOrBlank()) return@setOnClickListener
            else {
                viewModel.sendMessage(msg.trim().toString())
                editText.text = null
                messagesRv.smoothScrollToPosition(adapter.itemCount)
            }
        }
    }
}
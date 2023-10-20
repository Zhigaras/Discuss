package com.zhigaras.messaging.ui

import com.zhigaras.core.UiState
import com.zhigaras.messaging.databinding.MessageLayoutBinding
import com.zhigaras.messaging.domain.model.Message

interface MessagesUiState : UiState<MessageLayoutBinding> {
    
    fun handle(adapter: MessagesAdapter)
    
    // TODO: the same implementation. remove one if will remain the same
    class MessageReceived(private val data: List<Message>) : MessagesUiState {
        
        override fun update(binding: MessageLayoutBinding) = Unit
        
        override fun handle(adapter: MessagesAdapter) {
            adapter.setData(data)
        }
    }
    
    class MessageSent(private val data: List<Message>) : MessagesUiState {
        
        override fun update(binding: MessageLayoutBinding) = Unit
        
        override fun handle(adapter: MessagesAdapter) {
            adapter.setData(data)
        }
    }
}
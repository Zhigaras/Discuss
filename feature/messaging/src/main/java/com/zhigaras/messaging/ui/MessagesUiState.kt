package com.zhigaras.messaging.ui

import com.zhigaras.adapterdelegate.CompositeAdapter
import com.zhigaras.core.UiState
import com.zhigaras.messaging.databinding.MessageLayoutBinding
import com.zhigaras.messaging.domain.model.Message

interface MessagesUiState : UiState<MessageLayoutBinding> {
    
    fun handle(adapter: CompositeAdapter)
    
    // TODO: the same implementation. remove one if will remain the same
    class MessageReceived(private val data: List<Message>) : MessagesUiState {
        
        override fun update(binding: MessageLayoutBinding) = Unit
        
        override fun handle(adapter: CompositeAdapter) {
            adapter.submitList(data)
        }
    }
    
    class MessageSent(private val data: List<Message>) : MessagesUiState {
        
        override fun update(binding: MessageLayoutBinding) = Unit
        
        override fun handle(adapter: CompositeAdapter) {
            adapter.submitList(data)
        }
    }
}
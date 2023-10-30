package com.zhigaras.messaging.domain

import androidx.lifecycle.LifecycleOwner
import com.zhigaras.messaging.domain.model.Message
import com.zhigaras.messaging.ui.MessagesUiState
import java.util.Collections

interface MessagesInteractor {
    
    fun sendMessage(text: String): MessagesUiState
    
    fun observe(owner: LifecycleOwner, communication: MessagesUiStateCommunication.Post)
    
    class Base(
        private val messaging: Messaging,
    ) : MessagesInteractor {
        
        private val messages = Collections.synchronizedList(mutableListOf<Message>())
        
        override fun sendMessage(text: String): MessagesUiState {
            messaging.sendMessage(text)
            messages.add(Message.Outgoing(text))
            return MessagesUiState.MessageSent(messages.toList())
        }
        
        override fun observe(
            owner: LifecycleOwner,
            communication: MessagesUiStateCommunication.Post
        ) {
            messaging.observe(owner) {
                messages.add(Message.Incoming(it))
                communication.postBackground(MessagesUiState.MessageReceived(messages.toList()))
            }
        }
    }
}

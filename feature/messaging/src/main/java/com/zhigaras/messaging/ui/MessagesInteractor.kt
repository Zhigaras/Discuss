package com.zhigaras.messaging.ui

import androidx.lifecycle.LifecycleOwner
import com.zhigaras.messaging.domain.MessagesUiStateCommunication
import com.zhigaras.messaging.domain.Messaging
import com.zhigaras.messaging.domain.model.Message

interface MessagesInteractor {
    
    fun sendMessage(text: String): MessagesUiState
    
    fun observe(owner: LifecycleOwner, communication: MessagesUiStateCommunication.Post)
    
    class Base(
        private val messaging: Messaging,
    ) : MessagesInteractor {
        
        private val messages = mutableListOf<Message>()
        
        override fun sendMessage(text: String): MessagesUiState {
            messaging.sendMessage(text)
            messages.add(Message.Outgoing(text))
            return MessagesUiState.MessageSent(messages)
        }
        
        override fun observe(
            owner: LifecycleOwner,
            communication: MessagesUiStateCommunication.Post
        ) {
            messaging.observe(owner) {
                messages.add(Message.Incoming(it))
                communication.postBackground(MessagesUiState.MessageReceived(messages))
            }
        }
        
        // TODO: unregister observer
    }
}

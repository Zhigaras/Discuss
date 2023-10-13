package com.zhigaras.calls.ui

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import com.zhigaras.calls.datachannel.model.Message
import com.zhigaras.calls.datachannel.model.MessagesCommunication
import com.zhigaras.calls.domain.Messaging

interface MessagesInteractor : MessagesCommunication.Observe {
    
    fun sendMessage(text: String)
    
    class Base(
        private val messaging: Messaging,
        private val communication: MessagesCommunication.Mutable
    ) : MessagesInteractor {
        
        private val messages = mutableListOf<Message>()
        
        override fun sendMessage(text: String) {
            messaging.sendMessage(text)
            messages.add(Message.Outgoing(text))
            communication.postBackground(messages)
        }
        
        override fun observe(owner: LifecycleOwner, observer: Observer<List<Message>>) {
            messaging.observe(owner) {
                messages.add(Message.Incoming(it))
                communication.postBackground(messages)
            }
            communication.observe(owner, observer)
        }
        
        // TODO: unregister observer
    }
}

package com.zhigaras.calls.ui

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import com.zhigaras.calls.datachannel.model.DataChannelCommunication
import com.zhigaras.calls.datachannel.model.Message
import com.zhigaras.calls.domain.Messaging

interface MessagesInteractor : DataChannelCommunication.Observe {
    
    fun sendMessage(text: String)
    
    class Base(
        private val messaging: Messaging,
        private val communication: DataChannelCommunication.Mutable
    ) : MessagesInteractor {
        
        private val messages = mutableListOf<Message>()
        
        override fun sendMessage(text: String) {
            messaging.sendMessage(text)
            messages.add(Message.Outgoing(text))
        }
        
        override fun observe(owner: LifecycleOwner, observer: Observer<String>) {
//            messaging.observe(owner) {
//                Log.d("QQQ interactor ", it)
//                messages.add(Message.Incoming(it))
//                communication.postBackground(messages)
//            }
            messaging.observe(owner, observer)
        }
        
        // TODO: unregister observer
    }
}

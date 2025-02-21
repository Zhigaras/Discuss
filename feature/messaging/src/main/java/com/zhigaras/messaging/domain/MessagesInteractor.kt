package com.zhigaras.messaging.domain

import com.zhigaras.messaging.domain.model.Message
import com.zhigaras.messaging.ui.MessagesUiState
import java.util.Collections

interface MessagesInteractor {

    fun sendMessage(text: String): MessagesUiState
    suspend fun observe(flux: MessagesUiStateFlux.Post): Nothing

    class Base(private val messaging: Messaging) : MessagesInteractor {

        private val messages = Collections.synchronizedList(mutableListOf<Message>())

        override fun sendMessage(text: String): MessagesUiState {
            messaging.sendMessage(text)
            messages.add(Message.Outgoing(text))
            return MessagesUiState.MessageSent(messages.toList())
        }

        override suspend fun observe(flux: MessagesUiStateFlux.Post): Nothing {
            messaging.collect {
                messages.add(Message.Incoming(it))
                flux.post(MessagesUiState.MessageReceived(messages.toList()))
            }
        }
    }
}

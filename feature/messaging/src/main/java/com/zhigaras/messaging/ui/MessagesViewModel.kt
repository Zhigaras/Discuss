package com.zhigaras.messaging.ui

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import com.zhigaras.core.BaseViewModel
import com.zhigaras.core.Dispatchers
import com.zhigaras.messaging.databinding.MessageLayoutBinding
import com.zhigaras.messaging.domain.MessagesUiStateCommunication
import com.zhigaras.messaging.domain.model.Message

class MessagesViewModel(
    private val messagesInteractor: MessagesInteractor,
    override val communication: MessagesUiStateCommunication.Mutable,
    dispatchers: Dispatchers
) : BaseViewModel<MessageLayoutBinding, MessagesUiState>(dispatchers) {
    
    fun sendMessage(text: String) {
        messagesInteractor.sendMessage(text)
    }
    
    fun observeMessages(owner: LifecycleOwner, observer: Observer<List<Message>>) {
        messagesInteractor.observe(owner, observer)
    }
}
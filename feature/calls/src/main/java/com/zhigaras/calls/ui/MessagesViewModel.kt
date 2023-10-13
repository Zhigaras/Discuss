package com.zhigaras.calls.ui

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import com.zhigaras.calls.datachannel.model.DataChannelCommunication
import com.zhigaras.calls.domain.MessagesUiStateCommunication
import com.zhigaras.core.BaseViewModel
import com.zhigaras.core.Dispatchers
import com.zhigaras.webrtc.databinding.MessageLayoutBinding

class MessagesViewModel(
    private val messagesInteractor: MessagesInteractor,
    override val communication: MessagesUiStateCommunication.Mutable,
    private val messagesCommunication: DataChannelCommunication.Mutable,
    dispatchers: Dispatchers
) : BaseViewModel<MessageLayoutBinding, MessagesUiState>(dispatchers) {
    
    fun sendMessage(text: String) {
        messagesInteractor.sendMessage(text)
    }
    
    fun observeMessages(owner: LifecycleOwner, observer: Observer<String>) {
        messagesInteractor.observe(owner, observer)
    }
}
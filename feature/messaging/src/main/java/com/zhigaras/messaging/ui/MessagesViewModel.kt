package com.zhigaras.messaging.ui

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import com.zhigaras.core.BaseViewModel
import com.zhigaras.core.Dispatchers
import com.zhigaras.messaging.domain.MessagesInteractor
import com.zhigaras.messaging.domain.MessagesUiStateCommunication

class MessagesViewModel(
    private val messagesInteractor: MessagesInteractor,
    override val communication: MessagesUiStateCommunication.Mutable,
    dispatchers: Dispatchers
) : BaseViewModel<MessagesUiState>(dispatchers) {
    
    fun sendMessage(text: String) {
        messagesInteractor.sendMessage(text).let { communication.postBackground(it) }
    }
    
    override fun observe(owner: LifecycleOwner, observer: Observer<MessagesUiState>) {
        messagesInteractor.observe(owner, communication)
        super.observe(owner, observer)
    }
}
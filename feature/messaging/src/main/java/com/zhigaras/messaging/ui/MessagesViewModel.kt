package com.zhigaras.messaging.ui

import com.zhigaras.core.BaseViewModel
import com.zhigaras.core.Dispatchers
import com.zhigaras.messaging.domain.MessagesInteractor
import com.zhigaras.messaging.domain.MessagesUiStateFlux
import kotlinx.coroutines.flow.FlowCollector

class MessagesViewModel(
    private val messagesInteractor: MessagesInteractor,
    private val uiStateFlux: MessagesUiStateFlux.Mutable,
    dispatchers: Dispatchers
) : BaseViewModel<MessagesUiState>(dispatchers, uiStateFlux) {

    fun sendMessage(text: String) {
        messagesInteractor.sendMessage(text).let { uiStateFlux.post(it) }
    }

    override suspend fun observeUiState(collector: FlowCollector<MessagesUiState>): Nothing {
        messagesInteractor.observe(uiStateFlux)
    }
}

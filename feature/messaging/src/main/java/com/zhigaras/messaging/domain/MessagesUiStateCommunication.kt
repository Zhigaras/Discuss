package com.zhigaras.messaging.domain

import com.zhigaras.core.Communication
import com.zhigaras.messaging.ui.MessagesUiState

interface MessagesUiStateCommunication {
    
    interface Observe : Communication.Observe<MessagesUiState>
    interface Post : Communication.Post<MessagesUiState>
    interface Mutable : Communication.Mutable<MessagesUiState>, Post, Observe
    class Base : Communication.Regular<MessagesUiState>(), Mutable
}
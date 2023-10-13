package com.zhigaras.calls.domain

import com.zhigaras.calls.ui.MessagesUiState
import com.zhigaras.core.Communication

interface MessagesUiStateCommunication {
    
    interface Observe : Communication.Observe<MessagesUiState>
    interface Post : Communication.Post<MessagesUiState>
    interface Mutable : Communication.Mutable<MessagesUiState>, Post, Observe
    class Base : Communication.Regular<MessagesUiState>(), Mutable
}
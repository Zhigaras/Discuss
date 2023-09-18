package com.zhigaras.calls.domain

import com.zhigaras.core.Communication
import com.zhigaras.calls.ui.CallUiState

interface CallCommunication {
    
    interface Observe : Communication.Observe<CallUiState>
    interface Post : Communication.Post<CallUiState>
    interface Mutable : Communication.Mutable<CallUiState>, Post, Observe
    class Base : Communication.Regular<CallUiState>(), Mutable
}
package com.zhigaras.discuss.domain

import com.zhigaras.core.Communication
import com.zhigaras.discuss.presentation.MainUiState

interface MainUiStateCommunication {
    interface Observe : Communication.Observe<MainUiState>
    interface Post : Communication.Post<MainUiState>
    interface Mutable : Communication.Mutable<MainUiState>, Post, Observe
    class Base : Communication.Regular<MainUiState>(), Mutable
}
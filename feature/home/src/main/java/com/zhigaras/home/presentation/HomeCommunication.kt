package com.zhigaras.home.presentation

import com.zhigaras.core.Communication

interface HomeCommunication {
    
    interface Observe : Communication.Observe<HomeUiState>
    interface Post : Communication.Post<HomeUiState>
    interface Mutable : Communication.Mutable<HomeUiState>, Post, Observe
    class Base : Communication.Regular<HomeUiState>(), Mutable
}
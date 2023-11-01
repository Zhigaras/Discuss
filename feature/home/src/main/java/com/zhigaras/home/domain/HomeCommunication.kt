package com.zhigaras.home.domain

import com.zhigaras.core.Communication
import com.zhigaras.home.presentation.home.HomeUiState

interface HomeCommunication {
    
    interface Observe : Communication.Observe<HomeUiState>
    interface Post : Communication.Post<HomeUiState>
    interface Mutable : Communication.Mutable<HomeUiState>, Post, Observe
    class Base : Communication.Regular<HomeUiState>(), Mutable
}
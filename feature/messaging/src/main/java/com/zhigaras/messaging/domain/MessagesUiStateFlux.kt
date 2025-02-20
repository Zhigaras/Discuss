package com.zhigaras.messaging.domain

import com.zhigaras.core.StateFlux
import com.zhigaras.messaging.ui.MessagesUiState

interface MessagesUiStateFlux {
    interface Observe : StateFlux.Observe<MessagesUiState>
    interface Post : StateFlux.Post<MessagesUiState>
    interface Mutable : StateFlux.Mutable<MessagesUiState>, Post, Observe
    class Base : StateFlux.Base<MessagesUiState>(MessagesUiState.Initial()), Mutable
}

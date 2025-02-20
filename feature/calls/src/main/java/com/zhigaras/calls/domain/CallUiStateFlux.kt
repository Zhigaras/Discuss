package com.zhigaras.calls.domain

import com.zhigaras.core.StateFlux
import com.zhigaras.calls.ui.CallUiState

interface CallUiStateFlux {
    interface Observe : StateFlux.Observe<CallUiState>
    interface Post : StateFlux.Post<CallUiState>
    interface Mutable : StateFlux.Mutable<CallUiState>, Post, Observe
    class Base : StateFlux.Base<CallUiState>(CallUiState.Initial()), Mutable
}

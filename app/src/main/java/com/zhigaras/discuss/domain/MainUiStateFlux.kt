package com.zhigaras.discuss.domain

import com.zhigaras.core.StateFlux
import com.zhigaras.discuss.presentation.MainUiState

interface MainUiStateFlux {
    interface Observe : StateFlux.Observe<MainUiState>
    interface Post : StateFlux.Post<MainUiState>
    interface Mutable : StateFlux.Mutable<MainUiState>, Post, Observe
    class Base : StateFlux.Base<MainUiState>(MainUiState.Initial()), Mutable
}

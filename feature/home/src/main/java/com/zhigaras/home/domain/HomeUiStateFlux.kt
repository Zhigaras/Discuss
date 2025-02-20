package com.zhigaras.home.domain

import com.zhigaras.core.StateFlux
import com.zhigaras.home.presentation.HomeUiState

interface HomeUiStateFlux {
    interface Observe : StateFlux.Observe<HomeUiState>
    interface Post : StateFlux.Post<HomeUiState>
    interface Mutable : StateFlux.Mutable<HomeUiState>, Post, Observe
    class Base : StateFlux.Base<HomeUiState>(HomeUiState.Initial()), Mutable
}

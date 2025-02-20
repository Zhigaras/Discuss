package com.zhigaras.profile.domain

import com.zhigaras.core.StateFlux
import com.zhigaras.profile.ui.ProfileUiState

interface ProfileUiStateFlux {
    interface Observe : StateFlux.Observe<ProfileUiState>
    interface Post : StateFlux.Post<ProfileUiState>
    interface Mutable : StateFlux.Mutable<ProfileUiState>, Post, Observe
    class Base : StateFlux.Base<ProfileUiState>(ProfileUiState.Initial()), Mutable
}

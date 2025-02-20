package com.zhigaras.home.domain

import com.zhigaras.core.StateFlux
import com.zhigaras.home.presentation.suggesttopic.SuggestTopicUiState

interface SuggestTopicUiStateFlux {
    interface Observe : StateFlux.Observe<SuggestTopicUiState>
    interface Post : StateFlux.Post<SuggestTopicUiState>
    interface Mutable : StateFlux.Mutable<SuggestTopicUiState>, Post, Observe
    class Base : StateFlux.Base<SuggestTopicUiState>(SuggestTopicUiState.Initial()), Mutable
}

package com.zhigaras.home.domain

import com.zhigaras.core.Communication
import com.zhigaras.home.presentation.suggesttopic.SuggestTopicUiState

interface SuggestTopicCommunication {
    
    interface Observe : Communication.Observe<SuggestTopicUiState>
    interface Post : Communication.Post<SuggestTopicUiState>
    interface Mutable : Communication.Mutable<SuggestTopicUiState>, Post, Observe
    class Base : Communication.Regular<SuggestTopicUiState>(), Mutable
}
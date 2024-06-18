package com.zhigaras.profile.domain

import com.zhigaras.core.Communication
import com.zhigaras.profile.ui.ProfileUiState

interface ProfileCommunication {
    
    interface Observe : Communication.Observe<ProfileUiState>
    interface Post : Communication.Post<ProfileUiState>
    interface Mutable : Communication.Mutable<ProfileUiState>, Post, Observe
    class Base : Communication.Regular<ProfileUiState>(), Mutable
}
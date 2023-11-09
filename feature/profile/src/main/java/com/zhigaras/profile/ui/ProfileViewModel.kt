package com.zhigaras.profile.ui

import com.zhigaras.core.BaseViewModel
import com.zhigaras.core.Dispatchers
import com.zhigaras.profile.domain.NavigateToSignIn
import com.zhigaras.profile.domain.ProfileCommunication
import com.zhigaras.profile.domain.ProfileInteractor

class ProfileViewModel(
    private val navigateToSignIn: NavigateToSignIn,
    private val profileInteractor: ProfileInteractor,
    override val uiCommunication: ProfileCommunication.Mutable,
    dispatchers: Dispatchers
) : BaseViewModel<ProfileUiState>(dispatchers) {
    
    fun logout() {
        uiCommunication.postUi(ProfileUiState.Progress())
        scopeLaunch(
            onBackground = { profileInteractor.logout() },
            onUi = { it.handle(uiCommunication, navigateToSignIn) }
        )
    }
}
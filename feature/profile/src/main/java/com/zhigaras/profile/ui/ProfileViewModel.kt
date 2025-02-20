package com.zhigaras.profile.ui

import com.zhigaras.core.BaseViewModel
import com.zhigaras.core.Dispatchers
import com.zhigaras.profile.domain.NavigateToSignIn
import com.zhigaras.profile.domain.ProfileUiStateFlux
import com.zhigaras.profile.domain.ProfileInteractor

class ProfileViewModel(
    private val navigateToSignIn: NavigateToSignIn,
    private val profileInteractor: ProfileInteractor,
    private val uiStateFlux: ProfileUiStateFlux.Mutable,
    dispatchers: Dispatchers
) : BaseViewModel<ProfileUiState>(dispatchers, uiStateFlux) {

    fun logout() {
        uiStateFlux.post(ProfileUiState.Progress())
        scopeLaunch(
            onBackground = { profileInteractor.logout() },
            onUi = { it.handle(uiStateFlux, navigateToSignIn) }
        )
    }
}

package com.zhigaras.discuss.presentation

import com.zhigaras.core.BaseViewModel
import com.zhigaras.core.Dispatchers
import com.zhigaras.core.NavigationFlux
import com.zhigaras.core.Screen
import com.zhigaras.discuss.domain.MainInteractor
import com.zhigaras.discuss.domain.MainUiStateFlux
import com.zhigaras.discuss.domain.NavigateToSignIn
import com.zhigaras.login.domain.IsUserAuthorized
import com.zhigaras.login.domain.NavigateToHome
import kotlinx.coroutines.flow.FlowCollector

class MainViewModel(
    private val mainInteractor: MainInteractor,
    private val navigationFlux: NavigationFlux.Observe,
    private val isUserAuthorized: IsUserAuthorized,
    private val navigateToSignIn: NavigateToSignIn,
    private val navigateToHome: NavigateToHome,
    private val uiStateFlux: MainUiStateFlux.Mutable,
    dispatchers: Dispatchers
) : BaseViewModel<MainUiState>(dispatchers, uiStateFlux) {

    suspend fun observeNavigation(collector: FlowCollector<Screen>) = navigationFlux.collect(collector)

    suspend fun init(isFirstRun: Boolean): Nothing {
        if (isFirstRun) {
            if (isUserAuthorized.isAuthorized()) navigateToHome.navigateToHome()
            else navigateToSignIn.navigateToSignIn()
        }
        mainInteractor.observeNetwork(uiStateFlux)
    }
}

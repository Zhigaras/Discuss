package com.zhigaras.discuss.presentation

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import com.zhigaras.core.BaseViewModel
import com.zhigaras.core.Dispatchers
import com.zhigaras.core.NavigationCommunication
import com.zhigaras.core.Screen
import com.zhigaras.discuss.domain.MainInteractor
import com.zhigaras.discuss.domain.MainUiStateCommunication
import com.zhigaras.discuss.domain.NavigateToSignIn
import com.zhigaras.login.domain.IsUserAuthorized
import com.zhigaras.login.domain.NavigateToHome

class MainViewModel(
    private val mainInteractor: MainInteractor,
    private val navigationCommunication: NavigationCommunication.Observe,
    private val isUserAuthorized: IsUserAuthorized,
    private val navigateToSignIn: NavigateToSignIn,
    private val navigateToHome: NavigateToHome,
    override val communication: MainUiStateCommunication.Mutable,
    dispatchers: Dispatchers
) : BaseViewModel<MainUiState>(dispatchers) {
    
    fun observeNavigation(owner: LifecycleOwner, observer: Observer<Screen>) {
        navigationCommunication.observe(owner, observer)
    }
    
    fun observeNetworkState(owner: LifecycleOwner, observer: Observer<MainUiState>) {
        communication.observe(owner, observer)
    }
    
    fun init(isFirstRun: Boolean, owner: LifecycleOwner) {
        if (isFirstRun) {
            if (isUserAuthorized.isAuthorized()) navigateToHome.navigateToHome()
            else navigateToSignIn.navigateToSignIn()
        }
        mainInteractor.observeNetwork(owner, communication)
    }
}
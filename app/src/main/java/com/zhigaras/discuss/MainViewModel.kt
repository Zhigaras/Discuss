package com.zhigaras.discuss

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import com.zhigaras.core.NavigationCommunication
import com.zhigaras.core.Screen
import com.zhigaras.login.domain.IsUserAuthorized
import com.zhigaras.login.domain.NavigateToHome

class MainViewModel(
    private val navigation: NavigationCommunication.Observe,
    private val isUserAuthorized: IsUserAuthorized,
    private val navigateToSignIn: NavigateToSignIn,
    private val navigateToHome: NavigateToHome
) : ViewModel(), NavigationCommunication.Observe {
    
    override fun observe(owner: LifecycleOwner, observer: Observer<Screen>) {
        navigation.observe(owner, observer)
    }
    
    fun init(isFirstRun: Boolean) {
        if (isFirstRun) {
            if (isUserAuthorized.isAuthorized()) navigateToHome.navigateToHome()
            else navigateToSignIn.navigateToSignIn()
        }
    }
}
package com.zhigaras.discuss

import com.zhigaras.core.NavigationCommunication
import com.zhigaras.login.domain.LoginRoutes
import com.zhigaras.login.domain.SignInScreen
import com.zhigaras.login.domain.SignUpScreen

interface AvailableRouts : MainRouts, LoginRoutes {
    
    class Base(
        private val navigation: NavigationCommunication.Post
    ) : AvailableRouts {
        
        override fun navigateToHome() = Unit // TODO: add homeScreen
        
        override fun navigateToSignIn() = navigation.post(SignInScreen)
        
        override fun navigateToSignUp() = navigation.post(SignUpScreen)
    }
}
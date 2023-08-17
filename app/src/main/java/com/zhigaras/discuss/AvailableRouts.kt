package com.zhigaras.discuss

import android.os.Bundle
import com.zhigaras.core.NavigationCommunication
import com.zhigaras.home.domain.HomeScreen
import com.zhigaras.login.domain.LoginRoutes
import com.zhigaras.login.domain.SignInScreen
import com.zhigaras.login.domain.SignUpScreen

interface AvailableRouts : MainRouts, LoginRoutes {
    
    class Base(
        private val navigation: NavigationCommunication.Post
    ) : AvailableRouts {
        
        override fun navigateToHome() = navigation.post(HomeScreen)
        
        override fun navigateToSignIn() = navigation.post(SignInScreen)
        
        override fun navigateToSignUp(args: Bundle?) = navigation.post(SignUpScreen(args = args))
    }
}
package com.zhigaras.discuss

import android.os.Bundle
import com.zhigaras.core.NavigationCommunication
import com.zhigaras.home.domain.HomeRoutes
import com.zhigaras.home.domain.HomeScreen
import com.zhigaras.login.domain.LoginRoutes
import com.zhigaras.login.domain.SignInScreen
import com.zhigaras.login.domain.SignUpScreen
import com.zhigaras.calls.domain.CallScreen

interface AvailableRouts : MainRouts, LoginRoutes, HomeRoutes {
    
    class Base(
        private val navigation: NavigationCommunication.Post
    ) : AvailableRouts {
        
        override fun navigateToHome() = navigation.post(HomeScreen)
        
        override fun navigateToCall() = navigation.post(CallScreen)
        
        override fun navigateToSignIn() = navigation.post(SignInScreen)
        
        override fun navigateToSignUp(args: Bundle?) = navigation.post(SignUpScreen(args = args))
    }
}
package com.zhigaras.discuss

import android.os.Bundle
import com.zhigaras.calls.domain.CallScreen
import com.zhigaras.core.NavigationCommunication
import com.zhigaras.home.domain.HomeRoutes
import com.zhigaras.home.domain.HomeScreen
import com.zhigaras.login.domain.LoginRoutes
import com.zhigaras.login.domain.SignInScreen
import com.zhigaras.login.domain.SignUpScreen

interface AvailableRouts : MainRouts, LoginRoutes, HomeRoutes {
    
    class Base(
        private val navigation: NavigationCommunication.Post
    ) : AvailableRouts {
        
        override fun navigateToHome() = navigation.postUi(HomeScreen)
        
        override fun navigateToCall(args: Bundle?) = navigation.postUi(CallScreen(args))
        
        override fun navigateToSignIn() = navigation.postUi(SignInScreen)
        
        override fun navigateToSignUp(args: Bundle?) = navigation.postUi(SignUpScreen(args))
    }
}
package com.zhigaras.discuss.domain

import android.os.Bundle
import com.zhigaras.calls.domain.CallRoutes
import com.zhigaras.calls.domain.CallScreen
import com.zhigaras.core.NavigationCommunication
import com.zhigaras.core.Screen
import com.zhigaras.home.domain.HomeRoutes
import com.zhigaras.home.domain.HomeScreen
import com.zhigaras.login.domain.LoginRoutes
import com.zhigaras.login.domain.signin.SignInScreen
import com.zhigaras.login.domain.signup.SignUpScreen
import com.zhigaras.profile.domain.ProfileRoutes
import com.zhigaras.profile.domain.ProfileScreen

interface AvailableRouts : MainRouts, LoginRoutes, HomeRoutes, CallRoutes, ProfileRoutes {
    
    class Base(private val navigation: NavigationCommunication.Post) : AvailableRouts {
        
        override fun navigateToHome() = navigation.postUi(HomeScreen)
        
        override fun navigateToCall(args: Bundle?) = navigation.postUi(CallScreen(args))
        
        override fun goBack() = navigation.postUi(Screen.PopBackStack)
        
        override fun navigateToSignIn() = navigation.postUi(SignInScreen)
        
        override fun navigateToSignUp(args: Bundle?) = navigation.postUi(SignUpScreen(args))
        
        override fun navigateToProfile() = navigation.postUi(ProfileScreen())
    }
}
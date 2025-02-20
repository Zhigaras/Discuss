package com.zhigaras.discuss.domain

import android.os.Bundle
import com.zhigaras.calls.domain.CallRoutes
import com.zhigaras.calls.domain.CallScreen
import com.zhigaras.core.NavigationFlux
import com.zhigaras.core.Screen
import com.zhigaras.home.domain.HomeRoutes
import com.zhigaras.home.domain.HomeScreen
import com.zhigaras.login.domain.LoginRoutes
import com.zhigaras.login.domain.signin.SignInScreen
import com.zhigaras.login.domain.signup.SignUpScreen
import com.zhigaras.profile.domain.ProfileRoutes
import com.zhigaras.profile.domain.ProfileScreen

interface AvailableRouts : MainRouts, LoginRoutes, HomeRoutes, CallRoutes, ProfileRoutes {

    class Base(private val navigation: NavigationFlux.Post) : AvailableRouts {
        override suspend fun navigateToHome() = navigation.emit(HomeScreen)
        override suspend fun navigateToCall(args: Bundle?) = navigation.emit(CallScreen(args))
        override suspend fun goBack() = navigation.emit(Screen.PopBackStack)
        override suspend fun navigateToSignIn() = navigation.emit(SignInScreen)
        override suspend fun navigateToSignUp(args: Bundle?) = navigation.emit(SignUpScreen(args))
        override suspend fun navigateToProfile() = navigation.emit(ProfileScreen())
    }
}

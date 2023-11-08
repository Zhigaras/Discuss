package com.zhigaras.profile.domain

interface ProfileRoutes : NavigateToSignInTwo

interface NavigateToSignInTwo {
    
    fun navigateToSignIn()
}
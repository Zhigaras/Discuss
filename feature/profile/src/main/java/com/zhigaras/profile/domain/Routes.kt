package com.zhigaras.profile.domain

interface ProfileRoutes : NavigateToSignIn

interface NavigateToSignIn {
    
    fun navigateToSignIn()
}
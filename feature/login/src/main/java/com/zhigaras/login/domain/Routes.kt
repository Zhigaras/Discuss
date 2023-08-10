package com.zhigaras.login.domain

interface LoginRoutes : NavigateToSignUp, NavigateToHome

interface NavigateToSignUp {
    
    fun navigateToSignUp()
}

interface NavigateToHome {
    
    fun navigateToHome()
}
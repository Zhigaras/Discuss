package com.zhigaras.login.domain

interface LoginRoutes : NavigateToSignUp

interface NavigateToSignUp {
    
    fun navigateToSignUp()
}
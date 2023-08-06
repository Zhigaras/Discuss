package com.zhigaras.discuss

interface MainRouts : NavigateToHome, NavigateToSignIn

interface NavigateToHome {
    
    fun navigateToHome()
}

interface NavigateToSignIn {
    
    fun navigateToSignIn()
}

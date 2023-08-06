package com.zhigaras.discuss

interface MainRouts : NavigateToHome, NavigateToSignIn, NavigateToSignUp

interface NavigateToHome {
    
    fun navigateToHome()
}

interface NavigateToSignIn {
    
    fun navigateToSignIn()
}

interface NavigateToSignUp {
    
    fun navigateToSignUp()
}

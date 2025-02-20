package com.zhigaras.discuss.domain

interface MainRouts : NavigateToSignIn

interface NavigateToSignIn {
    suspend fun navigateToSignIn()
}

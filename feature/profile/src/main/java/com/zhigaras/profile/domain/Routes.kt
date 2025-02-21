package com.zhigaras.profile.domain

interface ProfileRoutes : NavigateToSignIn

interface NavigateToSignIn {
    suspend fun navigateToSignIn()
}

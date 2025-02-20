package com.zhigaras.login.domain

import android.os.Bundle

interface LoginRoutes : NavigateToSignUp, NavigateToHome {
    companion object {
        const val EMAIL_KEY = "email_key"
    }
}

interface NavigateToSignUp {
    suspend fun navigateToSignUp(args: Bundle? = null)
}

interface NavigateToHome {
    suspend fun navigateToHome()
}

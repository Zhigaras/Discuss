package com.zhigaras.home.domain

import android.os.Bundle

interface HomeRoutes : NavigateToCall, NavigateToProfile

interface NavigateToCall {
    suspend fun navigateToCall(args: Bundle? = null)
}

interface NavigateToProfile {
    suspend fun navigateToProfile()
}

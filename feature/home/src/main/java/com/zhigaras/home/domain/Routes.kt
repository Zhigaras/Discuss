package com.zhigaras.home.domain

import android.os.Bundle

interface HomeRoutes : NavigateToCall, NavigateToProfile

interface NavigateToCall {
    
    fun navigateToCall(args: Bundle? = null)
}

interface NavigateToProfile {
    
    fun navigateToProfile()
}
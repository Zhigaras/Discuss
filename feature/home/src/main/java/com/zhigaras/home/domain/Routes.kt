package com.zhigaras.home.domain

import android.os.Bundle

interface HomeRoutes : NavigateToCall

interface NavigateToCall {
    
    fun navigateToCall(args: Bundle? = null)
}
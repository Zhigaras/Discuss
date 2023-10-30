package com.zhigaras.calls.domain

import com.zhigaras.core.GoBack

interface CallRoutes : GoBack {
    
    companion object {
        const val READY_TO_CALL_USER_KEY = "readyToCallUser"
    }
}
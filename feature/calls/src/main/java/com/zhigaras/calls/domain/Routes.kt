package com.zhigaras.calls.domain

import com.zhigaras.core.GoBack

interface CallRoutes : GoBack {
    
    companion object {
        const val SUBJECT_ID_KEY = "subjectIdKey"
        const val DISPUTE_POSITION_KEY = "disputePositionKey"
    }
}
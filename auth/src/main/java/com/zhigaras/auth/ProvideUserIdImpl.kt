package com.zhigaras.auth

import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.zhigaras.core.ProvideUserId

class ProvideUserIdImpl : ProvideUserId {
    
    private val auth = Firebase.auth
    
    override fun provide(): String { // TODO: handle exception in top level
        return auth.uid ?: throw IllegalStateException("User is not authorized")
    }
}
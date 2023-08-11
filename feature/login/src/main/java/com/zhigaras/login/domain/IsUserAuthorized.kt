package com.zhigaras.login.domain

import com.zhigaras.auth.Auth

interface IsUserAuthorized {
    
    fun isAuthorized(): Boolean
    
    class Base(private val auth: Auth) : IsUserAuthorized {
        
        override fun isAuthorized(): Boolean {
            return auth.isUserAuthorized()
        }
    }
}
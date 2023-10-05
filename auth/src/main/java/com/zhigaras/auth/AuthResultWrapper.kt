package com.zhigaras.auth

import androidx.activity.result.ActivityResult

interface AuthResultWrapper {
    
    fun idToken(client: OneTapSignInClient): String
    
    class Base(private val activityResult: ActivityResult) : AuthResultWrapper {
        
        override fun idToken(client: OneTapSignInClient): String {
            return client.getIdToken(activityResult)
        }
    }
}
package com.zhigaras.auth

import android.app.Activity
import androidx.activity.result.ActivityResult
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.common.api.ApiException

interface AuthResultWrapper {
    
    fun isSuccessful(): Boolean
    
    fun idToken(): String
    
    class Base(private val activityResult: ActivityResult) : AuthResultWrapper {
        
        override fun isSuccessful() = activityResult.resultCode == Activity.RESULT_OK
        
        override fun idToken(): String {
            val task = GoogleSignIn.getSignedInAccountFromIntent(activityResult.data)
            val account: GoogleSignInAccount = task.getResult(ApiException::class.java)
            val idToken = account.idToken
            return idToken ?: ""
        }
    }
}
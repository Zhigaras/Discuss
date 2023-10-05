package com.zhigaras.auth

import android.app.PendingIntent
import android.content.Context
import androidx.activity.result.ActivityResult
import com.google.android.gms.auth.api.identity.BeginSignInRequest
import com.google.android.gms.auth.api.identity.Identity
import com.google.android.gms.auth.api.identity.SignInClient
import kotlinx.coroutines.tasks.await

class OneTapSignInClient(context: Context) {
    
    private val client: SignInClient = Identity.getSignInClient(context)
    
    private class OneTapSignInRequest {
        
        private val options = BeginSignInRequest.GoogleIdTokenRequestOptions.builder()
            .setSupported(true)
            .setServerClientId(WEB_CLIENT_ID)
            .setFilterByAuthorizedAccounts(false)
            .build()
        
        fun getRequest(): BeginSignInRequest {
            return BeginSignInRequest.builder().setGoogleIdTokenRequestOptions(options)
                .setAutoSelectEnabled(true)
                .build()
        }
        
        private companion object {
            const val WEB_CLIENT_ID =
                "82587152369-a7sre2ouqbdtfnv1e2nc6b4l39o1k61e.apps.googleusercontent.com"
        }
    }
    
    suspend fun getIntent(): PendingIntent {
        return client.beginSignIn(OneTapSignInRequest().getRequest()).await().pendingIntent
    }
    
    fun getIdToken(result: ActivityResult): String {
        return client.getSignInCredentialFromIntent(result.data).googleIdToken ?: ""
    }
}
package com.zhigaras.auth

import android.content.Context
import android.content.Intent
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions

class OneTapSignInClient {
    
    private val options by lazy {
        GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken("82587152369-a7sre2ouqbdtfnv1e2nc6b4l39o1k61e.apps.googleusercontent.com")
            .requestEmail()
            .requestProfile()
            .build()
    }
    
    fun getIntent(context: Context): Intent {
        return GoogleSignIn.getClient(context, options).signInIntent
    }
}
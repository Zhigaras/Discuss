package com.zhigaras.login.domain.signin

import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.IntentSenderRequest
import com.zhigaras.auth.AuthResultWrapper
import com.zhigaras.auth.OneTapSignInClient

interface SignInRepository {
    
    suspend fun signInWithEmailAndPassword(email: String, password: String): SignInResult
    
    suspend fun handleOneTapSignInLaunch(
        launcher: ActivityResultLauncher<IntentSenderRequest>,
        client: OneTapSignInClient
    ): SignInResult
    
    suspend fun handelOneTapSignInResult(
        authResult: AuthResultWrapper,
        client: OneTapSignInClient
    ): SignInResult
}
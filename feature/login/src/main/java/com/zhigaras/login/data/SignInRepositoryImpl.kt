package com.zhigaras.login.data

import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.IntentSenderRequest
import com.zhigaras.auth.Auth
import com.zhigaras.auth.AuthResultWrapper
import com.zhigaras.auth.DiscussException
import com.zhigaras.auth.OneTapSignIn
import com.zhigaras.auth.OneTapSignInClient
import com.zhigaras.login.domain.signin.SignInRepository
import com.zhigaras.login.domain.signin.SignInResult

class SignInRepositoryImpl(private val auth: Auth, private val oneTapSignIn: OneTapSignIn) :
    SignInRepository {
    
    override suspend fun signInWithEmailAndPassword(email: String, password: String): SignInResult {
        return try {
            val user = auth.signInWithEmailAndPassword(email, password)
            SignInResult.Success(user)
        } catch (e: DiscussException) {
            SignInResult.Error(e.errorId())
        }
    }
    
    override suspend fun handleOneTapSignInLaunch(
        launcher: ActivityResultLauncher<IntentSenderRequest>,
        client: OneTapSignInClient
    ): SignInResult {
        return try {
            oneTapSignIn.handleBegin(launcher, client)
            SignInResult.OneTapSignInLaunched
        } catch (e: DiscussException) {
            SignInResult.Error(e.errorId())
        }
    }
    
    override suspend fun handelOneTapSignInResult(
        authResult: AuthResultWrapper,
        client: OneTapSignInClient
    ): SignInResult {
        return try {
            val user = oneTapSignIn.handleResult(authResult, client)
            SignInResult.Success(user)
        } catch (e: DiscussException) {
            SignInResult.Error(e.errorId())
        }
    }
}
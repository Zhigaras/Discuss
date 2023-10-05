package com.zhigaras.login.data

import com.zhigaras.auth.Auth
import com.zhigaras.auth.AuthResultWrapper
import com.zhigaras.auth.DiscussException
import com.zhigaras.auth.R
import com.zhigaras.login.domain.signin.SignInRepository
import com.zhigaras.login.domain.signin.SignInResult

class SignInRepositoryImpl(private val auth: Auth) : SignInRepository {
    
    override suspend fun signInWithEmailAndPassword(email: String, password: String): SignInResult {
        return try {
            val user = auth.signInWithEmailAndPassword(email, password)
            SignInResult.Success(user)
        } catch (e: DiscussException) {
            SignInResult.Error(e.errorId())
        }
    }
    
    override suspend fun signInWithGoogle(authResult: AuthResultWrapper): SignInResult {
        return if (authResult.isSuccessful()) {
            try {
                val user = auth.signInWithGoogle(authResult.idToken())
                SignInResult.Success(user)
            } catch (e: DiscussException) {
                SignInResult.Error(e.errorId())
            }
        } else {
            SignInResult.Error(R.string.something_went_wrong)
        }
    }
}
package com.zhigaras.login.data

import com.zhigaras.auth.Auth
import com.zhigaras.auth.DiscussException
import com.zhigaras.login.presentation.signin.domain.SignInRepository
import com.zhigaras.login.presentation.signin.domain.SignInResult

class SignInRepositoryImpl(
    private val auth: Auth
) : SignInRepository {
    
    override suspend fun signInWithEmailAndPassword(email: String, password: String): SignInResult {
        return try {
            auth.signInWithEmailAndPassword(email, password)
            SignInResult.Success
        } catch (e: DiscussException) {
            SignInResult.Error(e.errorId())
        }
    }
}
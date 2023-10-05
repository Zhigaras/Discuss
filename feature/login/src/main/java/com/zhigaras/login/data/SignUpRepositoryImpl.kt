package com.zhigaras.login.data

import com.zhigaras.auth.Auth
import com.zhigaras.auth.DiscussException
import com.zhigaras.login.domain.signup.SignUpResult
import com.zhigaras.login.domain.signup.SignUpRepository

class SignUpRepositoryImpl(
    private val auth: Auth
) : SignUpRepository {
    
    override suspend fun signUpWithEmailAndPassword(
        email: String,
        password: String
    ): SignUpResult {
        return try {
            val user = auth.signUpWithEmailAndPassword(email, password)
            SignUpResult.Success(user)
        } catch (e: DiscussException) {
            SignUpResult.Error(e.errorId())
        }
    }
}
package com.zhigaras.login.data

import com.zhigaras.auth.Auth
import com.zhigaras.auth.DiscussException
import com.zhigaras.login.domain.AuthResult
import com.zhigaras.login.domain.SignUpRepository

class SignUpRepositoryImpl(
    private val auth: Auth
) : SignUpRepository {
    
    override suspend fun signUpWithEmailAndPassword(
        email: String,
        password: String
    ): AuthResult {
        return try {
            val user = auth.signUpWithEmailAndPassword(email, password)
            AuthResult.Success(user)
        } catch (e: DiscussException) {
            AuthResult.Error(e.errorId())
        }
    }
}
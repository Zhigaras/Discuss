package com.zhigaras.login.domain

interface SignUpRepository {
    
    suspend fun signUpWithEmailAndPassword(email: String, password: String): AuthResult
}
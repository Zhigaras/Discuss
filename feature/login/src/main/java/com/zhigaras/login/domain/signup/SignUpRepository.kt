package com.zhigaras.login.domain.signup

interface SignUpRepository {
    
    suspend fun signUpWithEmailAndPassword(email: String, password: String): SignUpResult
}
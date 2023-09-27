package com.zhigaras.login.presentation.signup.domain

interface SignUpRepository {
    
    suspend fun signUpWithEmailAndPassword(email: String, password: String): SignUpResult
}
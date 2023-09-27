package com.zhigaras.login.presentation.signin.domain

interface SignInRepository {
    
    suspend fun signInWithEmailAndPassword(email: String, password: String): SignInResult
}
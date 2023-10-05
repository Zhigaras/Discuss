package com.zhigaras.login.domain.signin

import com.zhigaras.auth.AuthResultWrapper

interface SignInRepository {
    
    suspend fun signInWithEmailAndPassword(email: String, password: String): SignInResult
    
    suspend fun signInWithGoogle(authResult: AuthResultWrapper): SignInResult
}
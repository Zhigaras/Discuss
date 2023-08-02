package com.zhigaras.auth

interface Auth {
    
    suspend fun signUpWithEmailAndPassword(email: String, password: String)
    
    suspend fun signInWithEmailAndPassword(email: String, password: String)
    
    suspend fun signUpWithGoogle(token: String)
    
    suspend fun resetPassword(email: String)
    
}
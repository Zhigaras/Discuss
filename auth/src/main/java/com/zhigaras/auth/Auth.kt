package com.zhigaras.auth

interface Auth {
    
    suspend fun signUpWithEmailAndPassword(email: String, password: String): UserDto
    
    suspend fun signInWithEmailAndPassword(email: String, password: String): UserDto
    
    suspend fun signInWithGoogle(token: String): UserDto
    
    suspend fun resetPassword(email: String)
    
    fun isUserAuthorized(): Boolean
    
    fun logout()
    
}
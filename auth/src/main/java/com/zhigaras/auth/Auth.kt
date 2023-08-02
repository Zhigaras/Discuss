package com.zhigaras.auth

import com.google.firebase.auth.FirebaseUser

interface Auth {
    
    suspend fun signUpWithEmailAndPassword(email: String, password: String): FirebaseUser
    
    suspend fun signInWithEmailAndPassword(email: String, password: String): FirebaseUser
    
    suspend fun signUpWithGoogle(token: String): FirebaseUser
    
    suspend fun resetPassword(email: String)
    
}
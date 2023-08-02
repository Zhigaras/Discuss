package com.zhigaras.auth

import com.google.firebase.FirebaseTooManyRequestsException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.tasks.await

class AuthRepository: Auth {
    
    private val auth: FirebaseAuth = Firebase.auth
    
    override suspend fun signUpWithEmailAndPassword(email: String, password: String): FirebaseUser {
        try {
            val user = auth.createUserWithEmailAndPassword(email, password).await().user!!
            user.sendEmailVerification()
            return user
        } catch (e: FirebaseAuthUserCollisionException) {
            throw EmailIsUsed()
        } catch (e: Exception) {
            throw RegistrationFailed()
        }
    }
    
    override suspend fun signInWithEmailAndPassword(email: String, password: String): FirebaseUser {
        try {
            return auth.signInWithEmailAndPassword(email, password).await().user!!
        } catch (e: FirebaseAuthInvalidUserException) {
            throw InvalidUser()
        } catch (e: FirebaseAuthInvalidCredentialsException) {
            throw InvalidCredentials()
        } catch (e: FirebaseTooManyRequestsException) {
            throw TooManyRequests()
        } catch (e: Exception) {
            throw SigningInFailed()
        }
    }
    
    override suspend fun signUpWithGoogle(token: String): FirebaseUser {
        val firebaseCredentials = GoogleAuthProvider.getCredential(token, null)
        try {
            return auth.signInWithCredential(firebaseCredentials).await().user!!
        } catch (e: Exception) {
            throw SigningInFailed()
        }
    }
    
    override suspend fun resetPassword(email: String) {
        try {
            auth.sendPasswordResetEmail(email).await()
        } catch (e: Exception) {
            throw NoSuchUser()
        }
    }
}
package com.zhigaras.auth

import android.util.Log
import com.google.firebase.FirebaseTooManyRequestsException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.tasks.await

class AuthRepository : Auth {
    
    private val auth: FirebaseAuth = Firebase.auth
    
    override suspend fun signUpWithEmailAndPassword(email: String, password: String): UserDto {
        try {
            val user = auth.createUserWithEmailAndPassword(email, password).await().user!!
            user.sendEmailVerification()
            return UserDto(user)
        } catch (e: FirebaseAuthUserCollisionException) {
            throw EmailIsUsed()
        } catch (e: Exception) {
            throw RegistrationFailed()
        }
    }
    
    override suspend fun signInWithEmailAndPassword(email: String, password: String): UserDto {
        try {
            val user = auth.signInWithEmailAndPassword(email, password).await().user!!
            return UserDto(user)
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
    
    override suspend fun signUpWithGoogle(token: String): UserDto {
        val firebaseCredentials = GoogleAuthProvider.getCredential(token, null)
        try {
            val user = auth.signInWithCredential(firebaseCredentials).await().user!!
            return UserDto(user)
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
    
    override fun isUserAuthorized(): Boolean {
        Log.d("AAAA", auth.currentUser?.uid.toString())
        return auth.currentUser != null
    }
}
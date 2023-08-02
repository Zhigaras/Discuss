package com.zhigaras.auth

import com.google.firebase.FirebaseTooManyRequestsException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.GoogleAuthProvider
import com.zhigaras.auth.model.EmailIsUsed
import com.zhigaras.auth.model.InvalidCredentials
import com.zhigaras.auth.model.InvalidUser
import com.zhigaras.auth.model.NoSuchUser
import com.zhigaras.auth.model.RegistrationFailed
import com.zhigaras.auth.model.SigningInFailed
import com.zhigaras.auth.model.TooManyRequests
import kotlinx.coroutines.tasks.await

class AuthRepository(private val auth: FirebaseAuth): Auth {
    
    override suspend fun signUpWithEmailAndPassword(email: String, password: String) {
        try {
            val user = auth.createUserWithEmailAndPassword(email, password).await().user
            user!!.sendEmailVerification()
        } catch (e: FirebaseAuthUserCollisionException) {
            throw EmailIsUsed()
        } catch (e: Exception) {
            throw RegistrationFailed()
        }
    }
    
    override suspend fun signInWithEmailAndPassword(email: String, password: String) {
        try {
            auth.signInWithEmailAndPassword(email, password).await().user
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
    
    override suspend fun signUpWithGoogle(token: String) {
        val firebaseCredentials = GoogleAuthProvider.getCredential(token, null)
        try {
            val user = auth.signInWithCredential(firebaseCredentials).await().user!!
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
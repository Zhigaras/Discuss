package com.zhigaras.auth

import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.IntentSenderRequest
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.common.api.CommonStatusCodes
import com.google.android.play.core.integrity.model.IntegrityErrorCode.TOO_MANY_REQUESTS

interface OneTapSignIn {
    
    suspend fun handleBegin(
        launcher: ActivityResultLauncher<IntentSenderRequest>,
        client: OneTapSignInClient
    )
    
    suspend fun handleResult(
        authResult: AuthResultWrapper,
        client: OneTapSignInClient
    ): UserDto
    
    class Base(private val auth: Auth) : OneTapSignIn {
        
        override suspend fun handleBegin(
            launcher: ActivityResultLauncher<IntentSenderRequest>,
            client: OneTapSignInClient
        ) {
            try {
                val signInResult = client.getIntent()
                try {
                    launcher.launch(IntentSenderRequest.Builder(signInResult).build())
                } catch (e: Exception) {
                    throw CouldNotStartOneTapSignIn()
                }
            } catch (e: ApiException) {
                if (e.statusCode == TOO_MANY_REQUESTS) throw TooManyOneTapRequests()
                throw NoGoogleAccountsFound()
            } catch (e: Exception) {
                throw CouldNotStartOneTapSignIn()
            }
        }
        
        override suspend fun handleResult(
            authResult: AuthResultWrapper,
            client: OneTapSignInClient
        ): UserDto {
            try {
                val token = authResult.idToken(client)
                if (token.isNotBlank()) {
                    return auth.signInWithGoogle(token)
                } else {
                    throw TokenNotReceived()
                }
            } catch (e: ApiException) {
                when (e.statusCode) {
                    CommonStatusCodes.NETWORK_ERROR -> throw NetworkException()
                    CommonStatusCodes.CANCELED -> throw OneTapSignInCanceled()
                    else -> throw CouldNotGetCredentials()
                }
            }
        }
    }
}
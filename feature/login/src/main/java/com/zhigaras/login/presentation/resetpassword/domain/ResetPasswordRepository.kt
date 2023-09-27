package com.zhigaras.login.presentation.resetpassword.domain

interface ResetPasswordRepository {
    
    suspend fun resetPassword(email: String): ResetPasswordResult
}
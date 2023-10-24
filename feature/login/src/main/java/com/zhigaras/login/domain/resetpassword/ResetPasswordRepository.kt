package com.zhigaras.login.domain.resetpassword

interface ResetPasswordRepository {
    
    suspend fun resetPassword(email: String): ResetPasswordResult
}
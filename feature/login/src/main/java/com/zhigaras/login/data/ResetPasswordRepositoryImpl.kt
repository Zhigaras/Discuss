package com.zhigaras.login.data

import com.zhigaras.auth.Auth
import com.zhigaras.auth.DiscussException
import com.zhigaras.login.domain.resetpassword.ResetPasswordRepository
import com.zhigaras.login.domain.resetpassword.ResetPasswordResult

class ResetPasswordRepositoryImpl(private val auth: Auth) : ResetPasswordRepository {
    
    override suspend fun resetPassword(email: String): ResetPasswordResult {
        return try {
            auth.resetPassword(email)
            ResetPasswordResult.Success
        } catch (e: DiscussException) {
            ResetPasswordResult.Error(e.errorId())
        }
    }
}
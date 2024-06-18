package com.zhigaras.profile.data

import com.zhigaras.auth.Auth
import com.zhigaras.auth.DiscussException
import com.zhigaras.profile.domain.LogoutResult
import com.zhigaras.profile.domain.ProfileRepository

class ProfileRepositoryImpl(private val auth: Auth) : ProfileRepository {
    
    override fun logout(): LogoutResult {
        return try {
            auth.logout()
            LogoutResult.Success()
        } catch (e: DiscussException) {
            LogoutResult.Error(e.errorId())
        }
    }
}
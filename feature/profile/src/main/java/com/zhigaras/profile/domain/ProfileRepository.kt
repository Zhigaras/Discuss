package com.zhigaras.profile.domain

interface ProfileRepository {
    
    fun logout(): LogoutResult
}
package com.zhigaras.profile.domain

interface ProfileInteractor {
    
    fun logout(): LogoutResult
    
    class Base(private val profileRepository: ProfileRepository) : ProfileInteractor {
        
        override fun logout(): LogoutResult {
            return profileRepository.logout()
        }
    }
}
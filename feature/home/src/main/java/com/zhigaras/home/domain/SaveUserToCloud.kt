package com.zhigaras.home.domain

import com.zhigaras.cloudservice.CloudService
import com.zhigaras.cloudservice.CloudService.Companion.USERS_PATH
import com.zhigaras.home.domain.model.User

interface SaveUserToCloud {
    
    suspend fun save(userId: String, user: User)
    
    class Base(private val cloudService: CloudService) : SaveUserToCloud {
        
        override suspend fun save(userId: String, user: User) =
            cloudService.post(user, USERS_PATH, userId)
    }
}
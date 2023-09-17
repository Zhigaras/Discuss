package com.zhigaras.home.domain

import com.zhigaras.cloudeservice.CloudService
import com.zhigaras.home.domain.model.User

interface SaveUserToCloud { // TODO: move to auth module
    
    suspend fun save(userId: String, user: User)
    
    class Base(private val cloudService: CloudService) : SaveUserToCloud {
        
        override suspend fun save(userId: String, user: User) =
            cloudService.postWithId(USERS_PATH, userId, user)
    }
    
    companion object {
        private const val USERS_PATH = "Users"
    }
}
package com.zhigaras.cloudservice

import kotlinx.coroutines.flow.Flow

interface CloudService {
    
    suspend fun postWithIdGenerating(obj: Any?, vararg children: String): String
    
    suspend fun <T : Any> getDataSnapshot(clazz: Class<T>, vararg children: String): T
    
    fun postMultipleLevels(obj: Any?, vararg children: String)
    
    fun <T : Any> subscribeMultipleLevels(clazz: Class<T>, vararg children: String): Flow<T>
    
    fun <T : Any> subscribeToListMultipleLevels(
        clazz: Class<T>,
        vararg children: String
    ): Flow<List<T>>
    
    fun addItemToList(item: String, vararg children: String)
    
    fun removeListItem(itemId: String, vararg children: String)
    
    companion object {
        const val USERS_PATH = "Users"
        const val TOPICS_PATH = "Topics"
        const val CONNECTION_DATA_PATH = "connectionData"
        const val OPPONENT_EVENT_PATH = "opponent"
    }
}
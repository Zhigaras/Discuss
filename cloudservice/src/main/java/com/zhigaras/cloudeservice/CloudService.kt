package com.zhigaras.cloudeservice

interface CloudService {
    
    interface Callback<T : Any> {
        
        fun provide(obj: T)
        
        fun error(message: String)
    }
    
    fun postWithId(path: String, id: String, obj: Any)
    
    suspend fun postWithIdGenerating(path: String, obj: Any): String
    
    suspend fun <T : Any> getDataSnapshot(path: String, child: String, clazz: Class<T>): T
    
    suspend fun <T : Any> getListAndUpdate(
        path: String,
        child: String,
        fieldId: String,
        block: MutableList<T>.() -> MutableList<T>
    )
    
    fun postMultipleLevels(obj: Any, vararg children: String)
    
    fun <T : Any> subscribeMultipleLevels(
        callback: Callback<T>,
        clazz: Class<T>,
        vararg children: String
    )
    
    fun removeListItem(obj: Any, vararg children: String)
    
    companion object {
        const val USERS_PATH = "Users"
        const val SUBJECTS_PATH = "Subjects"
        const val CONNECTION_EVENT_PATH = "connectionEvent"
    }
}
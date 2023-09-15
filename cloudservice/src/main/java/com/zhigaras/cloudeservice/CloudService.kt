package com.zhigaras.cloudeservice

interface CloudService {
    
    interface Callback<T : Any> {
        
        fun provide(obj: T)
        
        fun error(message: String)
    }
    
    fun postRootLevel(path: String, obj: Any) // TODO: remove??
    
    fun postWithId(path: String, id: String, obj: Any)
    
    suspend fun postWithIdGenerating(path: String, obj: Any): String
    
    suspend fun <T : Any> getDataSnapshot(path: String, child: String, clazz: Class<T>): T
    
    fun updateField(
        path: String,
        child: String,
        fieldId: String,
        fieldValue: Any
    ) // TODO: remove???
    
    suspend fun <T : Any> getListAndUpdate(
        path: String,
        child: String,
        fieldId: String,
        block: MutableList<T>.() -> MutableList<T>
    )
    
    fun <T : Any> subscribeToRootLevel(path: String, clazz: Class<T>, callback: Callback<T>)
    
    fun postMultipleLevels(obj: Any, vararg children: String)
    
    fun <T : Any> subscribeMultipleLevels(
        callback: Callback<T>,
        clazz: Class<T>,
        vararg children: String
    )
}
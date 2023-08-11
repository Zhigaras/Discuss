package com.zhigaras.cloudeservice

interface CloudService {
    
    interface Callback<T : Any> {
        
        fun provide(obj: List<T>)
        
        fun error(message: String)
    }
    
    fun postRootLevel(path: String, obj: Any) // TODO: remove??
    
    fun postWithId(path: String, id: String, obj: Any)
    
    suspend fun postWithIdGenerating(path: String, obj: Any): String
    
    fun updateField(
        path: String,
        child: String,
        fieldId: String,
        fieldValue: Any
    ) // TODO: remove???
    
    suspend fun <T : Any> getAndUpdateField(
        path: String,
        child: String,
        fieldId: String,
        block: (T) -> T
    )
    
    fun <T : Any> subscribeToRootLevel(path: String, clazz: Class<T>, callback: Callback<T>)
    
}
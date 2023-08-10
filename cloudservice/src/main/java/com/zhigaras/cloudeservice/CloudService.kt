package com.zhigaras.cloudeservice

interface CloudService {
    
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
    
}
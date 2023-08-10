package com.zhigaras.cloudeservice

interface CloudService {
    
    suspend fun postFirstLevel(path: String, obj: Any)
    
    fun updateField(path: String, child: String, fieldId: String, fieldValue: Any)
    
}
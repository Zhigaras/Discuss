package com.zhigaras.cloudeservice

import android.content.Context
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.tasks.await

class CloudServiceImpl(context: Context) : CloudService {
    
    private val reference = ProvideDatabase.Base(context).database()
    
    override fun postRootLevel(path: String, obj: Any) {
        reference.child(path).setValue(obj)
    }
    
    override fun postWithId(path: String, id: String, obj: Any) {
        reference.child(path).child(id).setValue(obj)
    }
    
    override suspend fun postWithIdGenerating(path: String, obj: Any): String {
        val result = reference.child(path).push()
        result.setValue(obj).await()
        return result.key!!
    }
    
    override suspend fun <T : Any> getDataSnapshot(
        path: String,
        child: String,
        clazz: Class<T>
    ): T {
        return reference.child(path).child(child).get().await()
            .getValue(clazz)!! // TODO: handle exceptions
    }
    
    override fun updateField(path: String, child: String, fieldId: String, fieldValue: Any) {
        reference.child(path).child(child).child(fieldId).setValue(fieldValue)
    }
    
    override suspend fun <T : Any> getListAndUpdate(
        path: String,
        child: String,
        fieldId: String,
        block: (MutableList<T>) -> MutableList<T>
    ) {
        val directRef = reference.child(path).child(child).child(fieldId)
        val value = directRef.get().await().value as? MutableList<T> ?: mutableListOf()
        val updatedValue = block.invoke(value)
        directRef.setValue(updatedValue)
    }
    
    override fun <T : Any> subscribeToRootLevel(
        path: String,
        clazz: Class<T>,
        callback: CloudService.Callback<T>
    ) {
        reference.child(path).addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val data = snapshot.children.mapNotNull { it.getValue(clazz) }
                callback.provide(data)
            }
            
            override fun onCancelled(error: DatabaseError) = callback.error(error.message)
        })
    }
}
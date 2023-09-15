package com.zhigaras.cloudeservice

import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.tasks.await

class CloudServiceImpl(provideDatabase: ProvideDatabase) : CloudService {
    
    private val reference = provideDatabase.database()
    
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
        block: MutableList<T>.() -> MutableList<T>
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
        // TODO: childListener??
        reference.child(path).addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
//                val data = snapshot.children.mapNotNull { it.getValue(clazz) }
//                callback.provide(data)
                // FIXME: fix list
            }
            
            override fun onCancelled(error: DatabaseError) = callback.error(error.message)
        })
    }
    
    override fun postMultipleLevels(obj: Any, vararg children: String) {
        var ref = reference
        children.forEach { ref = ref.child(it) }
        ref.setValue(obj)
    }
    
    override fun <T : Any> subscribeMultipleLevels(
        callback: CloudService.Callback<T>,
        clazz: Class<T>,
        vararg children: String
    ) {
        var ref = reference // TODO: move to separate fun, DRY
        children.forEach { ref = ref.child(it) }
        ref.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val result = snapshot.getValue(clazz)
                if (result == null) callback.error("Data is null")
                else callback.provide(result)
            }
            
            override fun onCancelled(error: DatabaseError) {
                callback.error(error.message)
            }
        })
    }
}
package com.zhigaras.cloudeservice

import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.tasks.await

class CloudServiceImpl(provideDatabase: ProvideDatabase) : CloudService {
    
    private val reference = provideDatabase.database()
    
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
        return reference.child(path).child(child).get().addOnSuccessListener {
        }.addOnCanceledListener {
            throw IllegalStateException("problem with data") // TODO: replace it
        }.await().getValue(clazz)!!
    }
    
    override fun postMultipleLevels(obj: Any?, vararg children: String) {
        makeReference(*children).setValue(obj)
    }
    
    override fun <T : Any> subscribeMultipleLevels(
        callback: CloudService.Callback<T>,
        clazz: Class<T>,
        vararg children: String
    ) {
        makeReference(*children).addValueEventListener(object : ValueEventListener {
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
    
    override fun addItemToList(item: String, vararg children: String) {
        val ref = makeReference(*children)
        ref.updateChildren(mapOf(item to "waiting"))
    }
    
    override fun removeListItem(itemId: String, vararg children: String) {
        val ref = makeReference(*children)
        ref.updateChildren(mapOf(itemId to null))
    }
    
    private fun makeReference(vararg children: String): DatabaseReference {
        var ref = reference
        children.forEach { ref = ref.child(it) }
        return ref
    }
}
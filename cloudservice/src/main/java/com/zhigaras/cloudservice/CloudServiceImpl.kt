package com.zhigaras.cloudservice

import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

class CloudServiceImpl(provideDatabase: ProvideDatabase) : CloudService {
    
    private val reference = provideDatabase.database()
    
    private val listeners =
        hashMapOf<CloudService.Callback<*>, Pair<DatabaseReference, ValueEventListener>>()
    
    override suspend fun postWithIdGenerating(obj: Any?, vararg children: String): String {
        return suspendCoroutine { cont ->
            makeReference(*children).push().setValue(obj) { error, ref ->
                error?.let { cont.resumeWithException(IllegalStateException(error.message)) }
                    ?: cont.resume(ref.key!!)
            }
        }
    }
    
    override suspend fun <T : Any> getDataSnapshot(
        clazz: Class<T>,
        vararg children: String
    ): T = suspendCoroutine { cont ->
        makeReference(*children).get().addOnSuccessListener { snapshot ->
            snapshot.getValue(clazz)?.let { cont.resume(it) }
                ?: cont.resumeWithException(IllegalStateException("Problem with deserialization"))
        }.addOnFailureListener {
            cont.resumeWithException(IllegalStateException("Problem with data"))
        }.addOnCanceledListener {
            cont.resumeWithException(IllegalStateException("Canceled"))
        }
    }
    
    override fun postMultipleLevels(obj: Any?, vararg children: String) {
        makeReference(*children).setValue(obj)
    }
    
    override fun <T : Any> subscribeMultipleLevels(
        callback: CloudService.Callback<T>,
        clazz: Class<T>,
        vararg children: String
    ) {
        val listener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val result = snapshot.getValue(clazz)
                if (result == null) callback.error("Data is null")
                else callback.provide(result)
            }
            
            override fun onCancelled(error: DatabaseError) {
                callback.error(error.message)
            }
        }
        val ref = makeReference(*children)
        listeners[callback] = ref to listener
        ref.addValueEventListener(listener)
    }
    
    override fun <T : Any> subscribeToListMultipleLevels(
        callback: CloudService.Callback<List<T>>,
        clazz: Class<T>,
        vararg children: String
    ) {
        val listener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val list = mutableListOf<T>()
                for (child in snapshot.children) {
                    child.getValue(clazz)?.let { list.add(it) }
                }
                callback.provide(list)
            }
            
            override fun onCancelled(error: DatabaseError) {
                callback.error(error.message)
            }
        }
        val ref = makeReference(*children)
        listeners[callback] = ref to listener
        ref.addValueEventListener(listener)
    }
    
    override fun addItemToList(item: String, vararg children: String) {
        val ref = makeReference(*children)
        ref.updateChildren(mapOf(item to "waiting")) // TODO: replace "waiting" away from here
    }
    
    override fun removeListItem(itemId: String, vararg children: String) {
        val ref = makeReference(*children)
        ref.updateChildren(mapOf(itemId to null))
    }
    
    override fun removeListener(callback: CloudService.Callback<*>) {
        val refAndListenerPair = listeners.remove(callback)
        refAndListenerPair?.first?.removeEventListener(refAndListenerPair.second)
    }
    
    private fun makeReference(vararg children: String): DatabaseReference {
        var ref = reference
        children.forEach { ref = ref.child(it) }
        return ref
    }
}
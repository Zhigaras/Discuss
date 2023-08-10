package com.zhigaras.cloudeservice

import android.content.Context
import com.google.android.gms.tasks.Task
import kotlinx.coroutines.tasks.await
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

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
    
    override fun updateField(path: String, child: String, fieldId: String, fieldValue: Any) {
        reference.child(path).child(child).child(fieldId).setValue(fieldValue)
    }
    
    override suspend fun <T : Any> getAndUpdateField(
        path: String,
        child: String,
        fieldId: String,
        block: (T) -> T
    ) {
        val directRef = reference.child(path).child(child).child(fieldId)
        val value = directRef.get().await().value as T
        val updatedValue = block.invoke(value)
        directRef.setValue(updatedValue)
    }
    
    private suspend fun handleResult(task: Task<Void>): Unit = suspendCoroutine { continuation ->
        task.addOnSuccessListener {
            continuation.resume(Unit)
        }.addOnFailureListener {
            continuation.resumeWithException(it)
        } // TODO: remove?
    }
}
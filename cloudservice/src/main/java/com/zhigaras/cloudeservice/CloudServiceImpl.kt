package com.zhigaras.cloudeservice

import android.content.Context
import com.google.android.gms.tasks.Task
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

class CloudServiceImpl(context: Context) : CloudService {
    
    private val reference = ProvideDatabase.Base(context).database()
    
    override suspend fun postFirstLevel(path: String, obj: Any) {
        handleResult(reference.setValue(obj))
    }
    
    override fun updateField(path: String, child: String, fieldId: String, fieldValue: Any) {
        TODO("Not yet implemented")
    }
    
    private suspend fun handleResult(task: Task<Void>): Unit = suspendCoroutine { continuation ->
        task.addOnSuccessListener {
            continuation.resume(Unit)
        }.addOnFailureListener {
            continuation.resumeWithException(it)
        }
    }
}
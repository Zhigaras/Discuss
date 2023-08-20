package com.zhigaras.core

import androidx.annotation.MainThread
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import java.util.concurrent.atomic.AtomicBoolean

interface Communication {
    
    interface Post<T : Any> {
        fun post(item: T)
    }
    
    interface Observe<T : Any> {
        fun observe(owner: LifecycleOwner, observer: Observer<T>)
    }
    
    interface Mutable<T : Any> : Post<T>, Observe<T>
    
    abstract class Abstract<T : Any>(private val liveData: MutableLiveData<T> = MutableLiveData()) :
        Mutable<T> {
        
        override fun post(item: T) {
            liveData.value = item
        }
        
        override fun observe(owner: LifecycleOwner, observer: Observer<T>) {
            liveData.observe(owner, observer)
        }
    }
    
    abstract class Single<T : Any> : Abstract<T>(SingleLiveEvent())
    
    abstract class Regular<T : Any> : Abstract<T>(MutableLiveData())
}

class SingleLiveEvent<T> : MutableLiveData<T>() {
    
    private val mPending = AtomicBoolean(false)
    
    @MainThread
    override fun observe(owner: LifecycleOwner, observer: Observer<in T>) {
        super.observe(owner) { t ->
            if (mPending.compareAndSet(true, false)) {
                observer.onChanged(t)
            }
        }
    }
    
    @MainThread
    override fun setValue(t: T) {
        mPending.set(true)
        super.setValue(t)
    }
}
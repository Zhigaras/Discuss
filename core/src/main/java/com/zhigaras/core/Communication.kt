package com.zhigaras.core

import androidx.annotation.MainThread
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import java.util.concurrent.atomic.AtomicBoolean

interface Communication {
    
    interface Post<T : Any> {
        fun postUi(item: T)
        
        fun postBackground(item: T)
    }
    
    interface Observe<T : Any> {
        fun observe(owner: LifecycleOwner, observer: Observer<T>)
    }
    
    interface ObserveForever<T : Any> {
        
        fun observeForever(observer: Observer<T>)
        
        fun removeObserver(observer: Observer<T>)
    }
    
    interface Mutable<T : Any> : Post<T>, Observe<T>, ObserveForever<T>
    
    abstract class Abstract<T : Any>(private val liveData: MutableLiveData<T> = MutableLiveData()) :
        Mutable<T> {
        
        override fun postUi(item: T) {
            liveData.value = item
        }
        
        override fun postBackground(item: T) {
            liveData.postValue(item)
        }
        
        override fun observe(owner: LifecycleOwner, observer: Observer<T>) {
            liveData.observe(owner, observer)
        }
        
        override fun observeForever(observer: Observer<T>) {
            liveData.observeForever(observer)
        }
        
        override fun removeObserver(observer: Observer<T>) {
            liveData.removeObserver(observer)
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
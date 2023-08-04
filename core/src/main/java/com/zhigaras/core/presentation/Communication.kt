package com.zhigaras.core.presentation

import androidx.annotation.MainThread
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer

interface Communication {
    
    interface Put<T : Any> {
        fun put(item: T)
    }
    
    interface Observe<T : Any> {
        fun observe(owner: LifecycleOwner, observer: Observer<T>)
    }
    
    interface Mutable<T : Any> : Put<T>, Observe<T>
    
    class Base<T : Any>(private val liveData: MutableLiveData<T>) : Mutable<T> {
        
        @MainThread
        override fun put(item: T) {
            liveData.value = item
        }
        
        override fun observe(owner: LifecycleOwner, observer: Observer<T>) {
            liveData.observe(owner, observer)
        }
    }
}
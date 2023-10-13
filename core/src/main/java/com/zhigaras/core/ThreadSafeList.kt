package com.zhigaras.core

class ThreadSafeList<E> {
    
    private val list = mutableListOf<E>()
    private val monitor = Any()
    
    fun add(item: E) {
        synchronized(monitor) {
            list.add(item)
        }
    }
    
    
}
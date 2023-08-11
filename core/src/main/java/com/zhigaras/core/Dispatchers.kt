package com.zhigaras.core

import kotlinx.coroutines.CoroutineDispatcher

interface Dispatchers {
    
    fun io(): CoroutineDispatcher
    fun ui(): CoroutineDispatcher
    
    class Base : Dispatchers {
        override fun io() = kotlinx.coroutines.Dispatchers.IO
        override fun ui() = kotlinx.coroutines.Dispatchers.Main
    }
}
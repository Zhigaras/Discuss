package com.zhigaras.core

import kotlinx.coroutines.CoroutineDispatcher

interface Dispatchers {
    
    fun io(): CoroutineDispatcher
    fun main(): CoroutineDispatcher
    
    class Base : Dispatchers {
        override fun io() = kotlinx.coroutines.Dispatchers.IO
        override fun main() = kotlinx.coroutines.Dispatchers.Main
    }
}
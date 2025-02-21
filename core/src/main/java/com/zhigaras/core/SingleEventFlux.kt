package com.zhigaras.core

import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.flow.MutableSharedFlow

interface SingleEventFlux {
    interface Post<T : Any> {
        suspend fun emit(item: T)
    }

    interface Observe<T : Any> {
        suspend fun collect(collector: FlowCollector<T>)
    }

    interface Mutable<T : Any> : Post<T>, Observe<T>

    abstract class Base<T : Any> : Mutable<T> {
        private val flow = MutableSharedFlow<T>()

        override suspend fun emit(item: T) = flow.emit(item)
        override suspend fun collect(collector: FlowCollector<T>) = flow.collect(collector)
    }
}

package com.zhigaras.core

import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.flow.MutableStateFlow

interface StateFlux {

    interface Post<T : Any> {
        fun post(item: T)
    }

    interface Observe<T : Any> {
        suspend fun collect(collector: FlowCollector<T>): Nothing
        fun current(): T
    }

    interface Mutable<T : Any> : Post<T>, Observe<T>

    abstract class Base<T : Any>(initial: T) : Mutable<T> {
        private val flow = MutableStateFlow(initial)
        override fun post(item: T) {
            flow.value = item
        }

        override suspend fun collect(collector: FlowCollector<T>): Nothing = flow.collect(collector)
        override fun current() = flow.value
    }
}

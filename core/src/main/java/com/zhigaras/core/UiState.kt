package com.zhigaras.core

import androidx.viewbinding.ViewBinding
import java.util.concurrent.atomic.AtomicBoolean

interface UiState<T : ViewBinding> {
    
    fun update(binding: T)
    
    abstract class SingleEvent<T : ViewBinding> : UiState<T> {
        
        private val needToBeShown = AtomicBoolean(true)
        
        protected abstract val block: T.() -> Unit
        
        override fun update(binding: T) {
            if (needToBeShown.compareAndSet(true, false)) {
                block.invoke(binding)
            }
        }
    }
}
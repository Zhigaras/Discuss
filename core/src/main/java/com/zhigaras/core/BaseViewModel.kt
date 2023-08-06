package com.zhigaras.core

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel

abstract class BaseViewModel<T : UiState>(
    protected val communication: Communication.Mutable<T>
) : ViewModel(), Communication.Observe<T> {
    
    override fun observe(owner: LifecycleOwner, observer: Observer<T>) {
        communication.observe(owner, observer)
    }
    
}
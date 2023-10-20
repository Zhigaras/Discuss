package com.zhigaras.core

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.viewbinding.ViewBinding
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

abstract class BaseViewModel<VB : ViewBinding, T : UiState<VB>>(
    private val dispatchers: Dispatchers,
) : ViewModel(), Communication.Observe<T> {
    
    protected abstract val communication: Communication.Mutable<T>
    
    override fun observe(owner: LifecycleOwner, observer: Observer<T>) {
        communication.observe(owner, observer)
    }
    
    protected fun <E> scopeLaunch(
        onBackground: suspend () -> E,
        onUi: suspend (E) -> Unit
    ) = viewModelScope.launch(dispatchers.io()) {
        val result = onBackground.invoke()
        withContext(dispatchers.main()) {
            onUi.invoke(result)
        }
    }
}
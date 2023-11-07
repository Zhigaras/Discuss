package com.zhigaras.core

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

abstract class BaseViewModel<T : UiState<*>>(
    private val dispatchers: Dispatchers,
) : ViewModel(), Communication.Observe<T> {
    
    protected abstract val uiCommunication: Communication.Mutable<T>
    
    override fun observe(owner: LifecycleOwner, observer: Observer<T>) {
        uiCommunication.observe(owner, observer)
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
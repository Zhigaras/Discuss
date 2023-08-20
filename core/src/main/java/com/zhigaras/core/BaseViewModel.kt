package com.zhigaras.core

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.viewbinding.ViewBinding
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

abstract class BaseViewModel<VB: ViewBinding, T : UiState<VB>>(
    protected val communication: Communication.Mutable<T>,
    private val dispatchers: Dispatchers
) : ViewModel(), Communication.Observe<T> {
    
    override fun observe(owner: LifecycleOwner, observer: Observer<T?>) {
        communication.observe(owner, observer)
    }
    
    protected fun <E> scopeLaunch(
        onLoading: () -> Unit = {},
        onSuccess: suspend (E) -> Unit = {},
        onError: suspend (e: DiscussException) -> Unit = {},
        payload: suspend () -> E,
    ) = viewModelScope.launch(dispatchers.ui()) {
        onLoading.invoke()
        try {
            withContext(dispatchers.io()) {
                val result = payload.invoke()
                withContext(dispatchers.ui()) {
                    onSuccess.invoke(result)
                }
            }
        } catch (e: DiscussException) {
            onError.invoke(e)
        }
    }
}
package com.zhigaras.core

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

abstract class BaseViewModel<T : UiState>(
    protected val communication: Communication.Mutable<T>,
    private val dispatchers: Dispatchers
) : ViewModel(), Communication.Observe<T> {
    
    override fun observe(owner: LifecycleOwner, observer: Observer<T?>) {
        communication.observe(owner, observer)
    }
    
    protected fun scopeLaunch(
        onLoading: () -> Unit = {},
        onSuccess: () -> Unit = {},
        onError: suspend (e: DiscussException) -> Unit = {},
        onFinally: () -> Unit = {}, // TODO remove?
        payload: suspend () -> Unit,
    ) = viewModelScope.launch(dispatchers.ui()) {
        onLoading.invoke()
        try {
            withContext(dispatchers.io()) {
                payload.invoke()
            }
            withContext(dispatchers.ui()) {
                onSuccess.invoke()
            }
        } catch (e: DiscussException) {
            onError.invoke(e)
        }
    }
}
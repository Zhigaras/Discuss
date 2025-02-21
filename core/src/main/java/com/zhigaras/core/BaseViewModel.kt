package com.zhigaras.core

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

abstract class BaseViewModel<out T : UiState<*>>(
    private val dispatchers: Dispatchers,
    private val uiStateFlux: StateFlux.Mutable<T>
) : ViewModel() {

    open suspend fun observeUiState(collector: FlowCollector<T>): Nothing = uiStateFlux.collect(collector)

    protected fun <E> scopeLaunch(
        onBackground: suspend () -> E,
        onUi: suspend (E) -> Unit
    ) = viewModelScope.launch(dispatchers.io()) {
        val result = onBackground.invoke()
        withContext(dispatchers.main()) { onUi.invoke(result) }
    }

    protected fun <E> safeLaunch(block: suspend () -> E) = viewModelScope.launch {
        block.invoke()
    }
}

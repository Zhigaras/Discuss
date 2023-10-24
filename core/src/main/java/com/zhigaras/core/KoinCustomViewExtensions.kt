package com.zhigaras.core

import android.view.View
import androidx.annotation.MainThread
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelStoreOwner
import androidx.lifecycle.findViewTreeViewModelStoreOwner
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.lifecycle.viewmodel.MutableCreationExtras
import org.koin.androidx.viewmodel.resolveViewModel
import org.koin.core.annotation.KoinInternalApi
import org.koin.core.parameter.ParametersHolder
import org.koin.core.qualifier.Qualifier
import org.koin.java.KoinJavaComponent

@MainThread
inline fun <reified T : ViewModel> View.viewModel(
    qualifier: Qualifier? = null,
    noinline ownerProducer: () -> ViewModelStoreOwner = { findViewTreeViewModelStoreOwner()!! },
    noinline extrasProducer: (() -> CreationExtras)? = null,
    noinline parameters: (() -> ParametersHolder)? = null,
): Lazy<T> {
    return lazy(LazyThreadSafetyMode.NONE) {
        getViewModel(qualifier, ownerProducer, extrasProducer, parameters)
    }
}

@OptIn(KoinInternalApi::class)
@MainThread
inline fun <reified T : ViewModel> View.getViewModel(
    qualifier: Qualifier? = null,
    noinline ownerProducer: () -> ViewModelStoreOwner = { findViewTreeViewModelStoreOwner()!! },
    noinline extrasProducer: (() -> CreationExtras)? = null,
    noinline parameters: (() -> ParametersHolder)? = null,
): T {
    return resolveViewModel(
        T::class,
        ownerProducer().viewModelStore,
        extras = extrasProducer?.invoke() ?: MutableCreationExtras(),
        qualifier = qualifier,
        parameters = parameters,
        scope = KoinJavaComponent.getKoin().scopeRegistry.rootScope
    )
}
package com.zhigaras.messaging.di

import com.zhigaras.messaging.domain.DataChannelStateFlux
import com.zhigaras.messaging.domain.MessagesUiStateFlux
import com.zhigaras.messaging.ui.MessagesViewModel
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.dsl.binds
import org.koin.dsl.module

fun messagesModule() = module {
    
    viewModelOf(::MessagesViewModel)
    
    factory { MessagesUiStateFlux.Base() } binds arrayOf(
        MessagesUiStateFlux.Mutable::class,
        MessagesUiStateFlux.Observe::class,
        MessagesUiStateFlux.Post::class,
    )
    
    factory { DataChannelStateFlux.Base() } binds arrayOf(
        DataChannelStateFlux.Observe::class,
        DataChannelStateFlux.Post::class,
        DataChannelStateFlux.Mutable::class
    )
}
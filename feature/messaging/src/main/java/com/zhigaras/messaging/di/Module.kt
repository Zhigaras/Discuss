package com.zhigaras.messaging.di

import com.zhigaras.messaging.domain.DataChannelCommunication
import com.zhigaras.messaging.domain.MessagesUiStateCommunication
import com.zhigaras.messaging.domain.MessagesInteractor
import com.zhigaras.messaging.ui.MessagesViewModel
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.dsl.bind
import org.koin.dsl.binds
import org.koin.dsl.module

fun messagesModule() = module {
    
    viewModelOf(::MessagesViewModel)
    
    factory { MessagesUiStateCommunication.Base() } binds arrayOf(
        MessagesUiStateCommunication.Mutable::class,
        MessagesUiStateCommunication.Observe::class,
        MessagesUiStateCommunication.Post::class,
    )
    
    factory { DataChannelCommunication.Base() } binds arrayOf(
        DataChannelCommunication.Observe::class,
        DataChannelCommunication.Post::class,
        DataChannelCommunication.Mutable::class
    )
    
    factory { MessagesInteractor.Base(get()) } bind MessagesInteractor::class
}
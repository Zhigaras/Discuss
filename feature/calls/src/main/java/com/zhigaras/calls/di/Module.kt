package com.zhigaras.calls.di

import com.zhigaras.calls.data.CallsCloudServiceImpl
import com.zhigaras.calls.datachannel.model.DataChannelCommunication
import com.zhigaras.calls.datachannel.model.MessagesCommunication
import com.zhigaras.calls.domain.CallCommunication
import com.zhigaras.calls.domain.CallsCloudService
import com.zhigaras.calls.domain.CallsController
import com.zhigaras.calls.domain.InitCalls
import com.zhigaras.calls.domain.MatchingInteractor
import com.zhigaras.calls.domain.MessagesUiStateCommunication
import com.zhigaras.calls.domain.Messaging
import com.zhigaras.calls.ui.CallViewModel
import com.zhigaras.calls.ui.MessagesInteractor
import com.zhigaras.calls.ui.MessagesViewModel
import com.zhigaras.calls.webrtc.PeerConnectionCallback
import org.koin.android.ext.koin.androidApplication
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.dsl.bind
import org.koin.dsl.binds
import org.koin.dsl.module

fun callModule() = module {
    
    viewModelOf(::CallViewModel)
    viewModelOf(::MessagesViewModel)
    
    single { CallCommunication.Base() } binds arrayOf(
        CallCommunication.Mutable::class,
        CallCommunication.Observe::class,
        CallCommunication.Post::class
    )
    
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
    
    factory { MessagesCommunication.Base() } binds arrayOf(
        MessagesCommunication.Observe::class,
        MessagesCommunication.Post::class,
        MessagesCommunication.Mutable::class
    )
    
    factory { MessagesInteractor.Base(get(), get()) } bind MessagesInteractor::class
    
    single { CallsController.Base(androidApplication(), get(), get(), get(), get()) } binds arrayOf(
        CallsController::class,
        InitCalls::class,
        Messaging::class
    )
    
    single { MatchingInteractor.Base(get()) } bind MatchingInteractor::class
    
    single { CallsCloudServiceImpl(get()) } bind CallsCloudService::class
    
    factory { PeerConnectionCallback(get()) } bind PeerConnectionCallback::class
}
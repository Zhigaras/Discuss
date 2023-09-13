package com.zhigaras.calls.di

import com.zhigaras.calls.domain.CallCommunication
import com.zhigaras.calls.ui.CallViewModel
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.dsl.binds
import org.koin.dsl.module

fun callModule() = module {
    
    viewModelOf(::CallViewModel)
    
    single { CallCommunication.Base() } binds arrayOf(
        CallCommunication.Mutable::class,
        CallCommunication.Observe::class,
        CallCommunication.Post::class
    )
}
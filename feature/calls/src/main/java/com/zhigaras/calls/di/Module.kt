package com.zhigaras.calls.di

import com.zhigaras.calls.data.CallsControllerImpl
import com.zhigaras.calls.domain.CallCommunication
import com.zhigaras.calls.domain.CallsController
import com.zhigaras.calls.ui.CallViewModel
import org.koin.android.ext.koin.androidApplication
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.dsl.bind
import org.koin.dsl.binds
import org.koin.dsl.module

fun callModule() = module {
    
    viewModelOf(::CallViewModel)
    
    single { CallCommunication.Base() } binds arrayOf(
        CallCommunication.Mutable::class,
        CallCommunication.Observe::class,
        CallCommunication.Post::class
    )
    
    single { CallsControllerImpl(androidApplication()) } bind CallsController::class
}
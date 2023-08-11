package com.zhigaras.home.di

import com.zhigaras.home.presentation.HomeCommunication
import com.zhigaras.home.presentation.HomeViewModel
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.dsl.binds
import org.koin.dsl.module

fun homeModule() = module {
    
    viewModelOf(::HomeViewModel)
    
    single { HomeCommunication.Base() } binds arrayOf(
        HomeCommunication.Mutable::class,
        HomeCommunication.Observe::class,
        HomeCommunication.Post::class
    )
}
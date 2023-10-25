package com.zhigaras.home.di

import com.zhigaras.cloudeservice.CloudService
import com.zhigaras.cloudeservice.CloudServiceImpl
import com.zhigaras.cloudeservice.ProvideDatabase
import com.zhigaras.home.domain.HomeCommunication
import com.zhigaras.home.domain.SaveUserToCloud
import com.zhigaras.home.presentation.HomeViewModel
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.dsl.bind
import org.koin.dsl.binds
import org.koin.dsl.module

fun homeModule() = module {
    
    viewModelOf(::HomeViewModel)
    
    factory { HomeCommunication.Base() } binds arrayOf(
        HomeCommunication.Mutable::class,
        HomeCommunication.Observe::class,
        HomeCommunication.Post::class
    )
    
    factory { CloudServiceImpl(get()) } bind CloudService::class
    
    factory { ProvideDatabase.Base() } bind ProvideDatabase::class
    
    factory { SaveUserToCloud.Base(get()) } bind SaveUserToCloud::class
}
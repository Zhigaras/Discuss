package com.zhigaras.home.di

import com.zhigaras.cloudservice.CloudService
import com.zhigaras.cloudservice.CloudServiceImpl
import com.zhigaras.cloudservice.ProvideDatabase
import com.zhigaras.home.data.HomeCloudServiceImpl
import com.zhigaras.home.domain.HomeCloudService
import com.zhigaras.home.presentation.suggesttopic.SuggestTopicViewModel
import com.zhigaras.home.domain.HomeCommunication
import com.zhigaras.home.domain.HomeInteractor
import com.zhigaras.home.domain.SaveUserToCloud
import com.zhigaras.home.domain.SuggestTopic
import com.zhigaras.home.domain.SuggestTopicCommunication
import com.zhigaras.home.presentation.home.HomeViewModel
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.dsl.bind
import org.koin.dsl.binds
import org.koin.dsl.module

fun homeModule() = listOf(suggestTopicModule(), module {
    
    viewModelOf(::HomeViewModel)
    
    factory { HomeCommunication.Base() } binds arrayOf(
        HomeCommunication.Mutable::class,
        HomeCommunication.Observe::class,
        HomeCommunication.Post::class
    )
    
    factory { HomeInteractor.Base(get(), get()) } binds arrayOf(
        HomeInteractor::class,
        SuggestTopic::class
    )
    
    factory { HomeCloudServiceImpl(get()) } bind HomeCloudService::class
    
    factory { CloudServiceImpl(get()) } bind CloudService::class
    
    factory { ProvideDatabase.Base() } bind ProvideDatabase::class
    
    factory { SaveUserToCloud.Base(get()) } bind SaveUserToCloud::class
})

fun suggestTopicModule() = module {
    
    viewModelOf(::SuggestTopicViewModel)
    
    factory { SuggestTopicCommunication.Base() } binds arrayOf(
        SuggestTopicCommunication.Mutable::class,
        SuggestTopicCommunication.Observe::class,
        SuggestTopicCommunication.Post::class
    )
}
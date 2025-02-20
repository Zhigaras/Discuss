package com.zhigaras.home.di

import com.zhigaras.cloudservice.CloudService
import com.zhigaras.cloudservice.CloudServiceImpl
import com.zhigaras.cloudservice.ProvideDatabase
import com.zhigaras.home.data.HomeCloudServiceImpl
import com.zhigaras.home.domain.HomeCloudService
import com.zhigaras.home.presentation.suggesttopic.SuggestTopicViewModel
import com.zhigaras.home.domain.HomeUiStateFlux
import com.zhigaras.home.domain.HomeInteractor
import com.zhigaras.home.domain.SaveUserToCloud
import com.zhigaras.home.domain.SuggestTopic
import com.zhigaras.home.domain.SuggestTopicUiStateFlux
import com.zhigaras.home.presentation.HomeViewModel
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.dsl.bind
import org.koin.dsl.binds
import org.koin.dsl.module

fun homeModule() = listOf(suggestTopicModule(), module {
    
    viewModelOf(::HomeViewModel)
    
    factory { HomeUiStateFlux.Base() } binds arrayOf(
        HomeUiStateFlux.Mutable::class,
        HomeUiStateFlux.Observe::class,
        HomeUiStateFlux.Post::class
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
    
    factory { SuggestTopicUiStateFlux.Base() } binds arrayOf(
        SuggestTopicUiStateFlux.Mutable::class,
        SuggestTopicUiStateFlux.Observe::class,
        SuggestTopicUiStateFlux.Post::class
    )
}
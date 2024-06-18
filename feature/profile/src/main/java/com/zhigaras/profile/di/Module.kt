package com.zhigaras.profile.di

import com.zhigaras.profile.data.ProfileRepositoryImpl
import com.zhigaras.profile.domain.ProfileCommunication
import com.zhigaras.profile.domain.ProfileInteractor
import com.zhigaras.profile.domain.ProfileRepository
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.dsl.module
import com.zhigaras.profile.ui.ProfileViewModel
import org.koin.dsl.bind
import org.koin.dsl.binds

fun profileModule() = module {
    
    viewModelOf(::ProfileViewModel)
    
    factory { ProfileCommunication.Base() } binds arrayOf(
        ProfileCommunication.Mutable::class,
        ProfileCommunication.Observe::class,
        ProfileCommunication.Post::class,
    )
    
    factory { ProfileInteractor.Base(get()) } bind ProfileInteractor::class
    
    factory { ProfileRepositoryImpl(get()) } bind ProfileRepository::class
}
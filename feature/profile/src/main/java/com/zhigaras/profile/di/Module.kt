package com.zhigaras.profile.di

import com.zhigaras.profile.data.ProfileRepositoryImpl
import com.zhigaras.profile.domain.ProfileUiStateFlux
import com.zhigaras.profile.domain.ProfileInteractor
import com.zhigaras.profile.domain.ProfileRepository
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.dsl.module
import com.zhigaras.profile.ui.ProfileViewModel
import org.koin.dsl.bind
import org.koin.dsl.binds

fun profileModule() = module {
    
    viewModelOf(::ProfileViewModel)
    
    factory { ProfileUiStateFlux.Base() } binds arrayOf(
        ProfileUiStateFlux.Mutable::class,
        ProfileUiStateFlux.Observe::class,
        ProfileUiStateFlux.Post::class,
    )
    
    factory { ProfileInteractor.Base(get()) } bind ProfileInteractor::class
    
    factory { ProfileRepositoryImpl(get()) } bind ProfileRepository::class
}
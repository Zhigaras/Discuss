package com.zhigaras.discuss

import com.zhigaras.core.NavigationCommunication
import com.zhigaras.login.domain.NavigateToSignUp
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.binds
import org.koin.dsl.module

fun mainModule() = module {
    
    viewModel { MainViewModel(get(), get()) }
    
    single { NavigationCommunication.Base() } binds arrayOf(
        NavigationCommunication.Mutable::class,
        NavigationCommunication.Observe::class,
        NavigationCommunication.Post::class
    )
    
    single { AvailableRouts.Base(get()) } binds arrayOf(
        NavigateToSignIn::class,
        NavigateToSignUp::class
    )
    
}
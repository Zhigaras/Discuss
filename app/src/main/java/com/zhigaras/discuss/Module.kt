package com.zhigaras.discuss

import com.zhigaras.calls.domain.CallRoutes
import com.zhigaras.core.Dispatchers
import com.zhigaras.core.NavigationCommunication
import com.zhigaras.home.domain.NavigateToCall
import com.zhigaras.login.domain.NavigateToHome
import com.zhigaras.login.domain.NavigateToSignUp
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.dsl.bind
import org.koin.dsl.binds
import org.koin.dsl.module

fun mainModule() = module {
    
    viewModelOf(::MainViewModel)
    
    single { NavigationCommunication.Base() } binds arrayOf(
        NavigationCommunication.Mutable::class,
        NavigationCommunication.Observe::class,
        NavigationCommunication.Post::class
    )
    
    factory { AvailableRouts.Base(get()) } binds arrayOf(
        NavigateToSignIn::class,
        NavigateToSignUp::class,
        NavigateToHome::class,
        NavigateToCall::class,
        CallRoutes::class
    )
    
    factory { Dispatchers.Base() } bind Dispatchers::class
    
}
package com.zhigaras.discuss.di

import android.content.Context
import android.net.ConnectivityManager
import com.zhigaras.calls.domain.CallRoutes
import com.zhigaras.core.Dispatchers
import com.zhigaras.core.NavigationFlux
import com.zhigaras.core.NetworkStateFlux
import com.zhigaras.core.NetworkHandler
import com.zhigaras.discuss.domain.AvailableRouts
import com.zhigaras.discuss.domain.MainInteractor
import com.zhigaras.discuss.domain.MainUiStateFlux
import com.zhigaras.discuss.domain.NavigateToSignIn
import com.zhigaras.discuss.presentation.MainViewModel
import com.zhigaras.home.domain.NavigateToCall
import com.zhigaras.home.domain.NavigateToProfile
import com.zhigaras.login.domain.NavigateToHome
import com.zhigaras.login.domain.NavigateToSignUp
import org.koin.android.ext.koin.androidApplication
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.dsl.bind
import org.koin.dsl.binds
import org.koin.dsl.module

fun mainModule() = module {
    
    viewModelOf(::MainViewModel)
    
    single { NavigationFlux.Base() } binds arrayOf(
        NavigationFlux.Mutable::class,
        NavigationFlux.Observe::class,
        NavigationFlux.Post::class
    )
    
    single {
        val connManager =
            androidApplication().getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        NetworkHandler.Base(connManager, get())
    } bind NetworkHandler::class
    
    single { NetworkStateFlux.Base() } binds arrayOf(
        NetworkStateFlux.Mutable::class,
        NetworkStateFlux.Observe::class,
        NetworkStateFlux.Post::class,
    )
    
    factory { MainUiStateFlux.Base() } binds arrayOf(
        MainUiStateFlux.Mutable::class,
        MainUiStateFlux.Observe::class,
        MainUiStateFlux.Post::class
    )
    
    factory { MainInteractor.Base(get()) } bind MainInteractor::class
    
    factory { AvailableRouts.Base(get()) } binds arrayOf(
        NavigateToSignIn::class,
        NavigateToSignUp::class,
        NavigateToHome::class,
        NavigateToCall::class,
        CallRoutes::class,
        com.zhigaras.profile.domain.NavigateToSignIn::class,
        NavigateToProfile::class
    )
    
    factory { Dispatchers.Base() } bind Dispatchers::class
    
}
package com.zhigaras.discuss.di

import android.content.Context
import android.net.ConnectivityManager
import com.zhigaras.calls.domain.CallRoutes
import com.zhigaras.core.Dispatchers
import com.zhigaras.core.NavigationCommunication
import com.zhigaras.core.NetworkCommunication
import com.zhigaras.core.NetworkHandler
import com.zhigaras.discuss.domain.AvailableRouts
import com.zhigaras.discuss.domain.MainInteractor
import com.zhigaras.discuss.domain.MainUiStateCommunication
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
    
    single { NavigationCommunication.Base() } binds arrayOf(
        NavigationCommunication.Mutable::class,
        NavigationCommunication.Observe::class,
        NavigationCommunication.Post::class
    )
    
    single {
        val connManager =
            androidApplication().getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        NetworkHandler.Base(connManager, get())
    } bind NetworkHandler::class
    
    single { NetworkCommunication.Base() } binds arrayOf(
        NetworkCommunication.Mutable::class,
        NetworkCommunication.Observe::class,
        NetworkCommunication.ObserveForever::class,
        NetworkCommunication.Post::class,
        NetworkCommunication.CurrentState::class
    )
    
    factory { MainUiStateCommunication.Base() } binds arrayOf(
        MainUiStateCommunication.Mutable::class,
        MainUiStateCommunication.Observe::class,
        MainUiStateCommunication.Post::class
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
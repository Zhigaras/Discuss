package com.zhigaras.discuss

import android.content.Context
import android.net.ConnectivityManager
import com.zhigaras.calls.domain.CallRoutes
import com.zhigaras.core.Dispatchers
import com.zhigaras.core.NavigationCommunication
import com.zhigaras.core.NetworkCommunication
import com.zhigaras.core.NetworkHandler
import com.zhigaras.home.domain.NavigateToCall
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
        NetworkCommunication.Post::class
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
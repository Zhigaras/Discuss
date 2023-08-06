package com.zhigaras.login.di

import com.zhigaras.auth.Auth
import com.zhigaras.auth.AuthRepository
import com.zhigaras.login.domain.SignInCommunication
import com.zhigaras.login.presentation.signin.SignInViewModel
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.dsl.bind
import org.koin.dsl.module

fun loginModule() = module {
    
    viewModelOf(::SignInViewModel)
    
    single { AuthRepository() } bind Auth::class
    
    single { SignInCommunication.Base() } bind SignInCommunication.Mutable::class
    
}
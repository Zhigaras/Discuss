package com.zhigaras.login.di

import com.zhigaras.auth.Auth
import com.zhigaras.auth.AuthRepository
import com.zhigaras.login.presentation.signin.SignInViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.bind
import org.koin.dsl.module

fun loginModule() = module {
    
    viewModel { SignInViewModel(get()) }
    
    single { AuthRepository() } bind Auth::class
    
}
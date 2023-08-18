package com.zhigaras.login.di

import com.zhigaras.auth.Auth
import com.zhigaras.auth.AuthRepository
import com.zhigaras.login.domain.IsUserAuthorized
import com.zhigaras.login.domain.ResetPasswordCommunication
import com.zhigaras.login.domain.ShowId
import com.zhigaras.login.domain.SignInCommunication
import com.zhigaras.login.domain.SignUpCommunication
import com.zhigaras.login.domain.UserMapper
import com.zhigaras.login.presentation.signin.SignInViewModel
import com.zhigaras.login.presentation.signup.SignUpViewModel
import com.zhigaras.login.presentation.resetpassword.ResetPasswordViewModel
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.dsl.bind
import org.koin.dsl.binds
import org.koin.dsl.module

fun loginModule() = module {
    
    viewModelOf(::SignInViewModel)
    viewModelOf(::SignUpViewModel)
    viewModelOf(::ResetPasswordViewModel)
    
    single { AuthRepository() } bind Auth::class
    
    single { SignInCommunication.Base() } binds arrayOf(
        SignInCommunication.Mutable::class,
        SignInCommunication.Observe::class,
        SignInCommunication.Post::class
    )
    
    single { SignUpCommunication.Base() } binds arrayOf(
        SignUpCommunication.Mutable::class,
        SignUpCommunication.Observe::class,
        SignUpCommunication.Post::class
    )
    
    single { ResetPasswordCommunication.Base() } binds arrayOf(
        ResetPasswordCommunication.Mutable::class,
        ResetPasswordCommunication.Observe::class,
        ResetPasswordCommunication.Post::class
    )
    
    single { IsUserAuthorized.Base(get()) } bind IsUserAuthorized::class
    
    single { UserMapper() }
    single { ShowId() }
}
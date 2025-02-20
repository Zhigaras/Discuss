package com.zhigaras.login.di

import com.zhigaras.auth.Auth
import com.zhigaras.auth.AuthRepository
import com.zhigaras.auth.OneTapSignIn
import com.zhigaras.core.ProvideUserId
import com.zhigaras.auth.ProvideUserIdImpl
import com.zhigaras.login.data.ResetPasswordRepositoryImpl
import com.zhigaras.login.data.SignInRepositoryImpl
import com.zhigaras.login.data.SignUpRepositoryImpl
import com.zhigaras.login.domain.IsUserAuthorized
import com.zhigaras.login.domain.resetpassword.ResetPasswordUiStateFlux
import com.zhigaras.login.domain.ShowId
import com.zhigaras.login.domain.signin.SignInUiStateFlux
import com.zhigaras.login.domain.signin.SignInRepository
import com.zhigaras.login.domain.signup.SignUpUiStateFlux
import com.zhigaras.login.domain.signup.SignUpRepository
import com.zhigaras.login.presentation.resetpassword.ResetPasswordViewModel
import com.zhigaras.login.domain.resetpassword.ResetPasswordRepository
import com.zhigaras.login.presentation.signin.SignInViewModel
import com.zhigaras.login.presentation.signup.SignUpViewModel
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.dsl.bind
import org.koin.dsl.binds
import org.koin.dsl.module

fun loginModule() = module {
    
    viewModelOf(::SignInViewModel)
    viewModelOf(::SignUpViewModel)
    viewModelOf(::ResetPasswordViewModel)
    
    factory { AuthRepository() } bind Auth::class
    
    factory { SignInUiStateFlux.Base() } binds arrayOf(
        SignInUiStateFlux.Mutable::class,
        SignInUiStateFlux.Observe::class,
        SignInUiStateFlux.Post::class
    )
    
    factory { SignUpUiStateFlux.Base() } binds arrayOf(
        SignUpUiStateFlux.Mutable::class,
        SignUpUiStateFlux.Observe::class,
        SignUpUiStateFlux.Post::class
    )
    
    factory { ResetPasswordUiStateFlux.Base() } binds arrayOf(
        ResetPasswordUiStateFlux.Mutable::class,
        ResetPasswordUiStateFlux.Observe::class,
        ResetPasswordUiStateFlux.Post::class
    )
    
    factory { OneTapSignIn.Base(get()) } bind OneTapSignIn::class
    
    factory { SignUpRepositoryImpl(get()) } bind SignUpRepository::class
    
    factory { SignInRepositoryImpl(get(), get()) } bind SignInRepository::class
    
    factory { ResetPasswordRepositoryImpl(get()) } bind ResetPasswordRepository::class
    
    factory { IsUserAuthorized.Base(get()) } bind IsUserAuthorized::class
    
    factory { ProvideUserIdImpl() } bind ProvideUserId::class
    
    factory { ShowId() }
}
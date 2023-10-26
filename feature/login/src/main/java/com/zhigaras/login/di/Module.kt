package com.zhigaras.login.di

import com.zhigaras.auth.Auth
import com.zhigaras.auth.AuthRepository
import com.zhigaras.auth.OneTapSignIn
import com.zhigaras.auth.ProvideUserId
import com.zhigaras.login.data.ResetPasswordRepositoryImpl
import com.zhigaras.login.data.SignInRepositoryImpl
import com.zhigaras.login.data.SignUpRepositoryImpl
import com.zhigaras.login.domain.IsUserAuthorized
import com.zhigaras.login.domain.resetpassword.ResetPasswordCommunication
import com.zhigaras.login.domain.ShowId
import com.zhigaras.login.domain.signin.SignInCommunication
import com.zhigaras.login.domain.signin.SignInRepository
import com.zhigaras.login.domain.signup.SignUpCommunication
import com.zhigaras.login.domain.signup.SignUpRepository
import com.zhigaras.login.domain.UserMapper
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
    
    factory { SignInCommunication.Base() } binds arrayOf(
        SignInCommunication.Mutable::class,
        SignInCommunication.Observe::class,
        SignInCommunication.Post::class
    )
    
    factory { SignUpCommunication.Base() } binds arrayOf(
        SignUpCommunication.Mutable::class,
        SignUpCommunication.Observe::class,
        SignUpCommunication.Post::class
    )
    
    factory { ResetPasswordCommunication.Base() } binds arrayOf(
        ResetPasswordCommunication.Mutable::class,
        ResetPasswordCommunication.Observe::class,
        ResetPasswordCommunication.Post::class
    )
    
    factory { OneTapSignIn.Base(get()) } bind OneTapSignIn::class
    
    factory { SignUpRepositoryImpl(get()) } bind SignUpRepository::class
    
    factory { SignInRepositoryImpl(get(), get()) } bind SignInRepository::class
    
    factory { ResetPasswordRepositoryImpl(get()) } bind ResetPasswordRepository::class
    
    factory { IsUserAuthorized.Base(get()) } bind IsUserAuthorized::class
    
    factory { ProvideUserId.Base() } bind ProvideUserId::class
    
    factory { UserMapper() }
    
    factory { ShowId() }
}
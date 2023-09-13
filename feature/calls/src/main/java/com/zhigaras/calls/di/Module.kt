package com.zhigaras.calls.di

import com.zhigaras.calls.ui.CallViewModel
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.dsl.module

fun callModule() = module {
    viewModelOf(::CallViewModel)
}
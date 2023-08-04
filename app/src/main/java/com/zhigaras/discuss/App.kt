package com.zhigaras.discuss

import android.app.Application
import com.google.firebase.crashlytics.ktx.crashlytics
import com.google.firebase.ktx.Firebase
import com.zhigaras.login.di.loginModule
import org.koin.core.context.startKoin

class App : Application() {
    
    override fun onCreate() {
        super.onCreate()
        Firebase.crashlytics.setCrashlyticsCollectionEnabled(false)
        startKoin {
            modules(
                listOf(loginModule())
            )
        }
    }
}
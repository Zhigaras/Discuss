package com.zhigaras.discuss

import android.app.Application
import com.google.firebase.FirebaseApp
import com.google.firebase.crashlytics.ktx.crashlytics
import com.google.firebase.ktx.Firebase
import com.zhigaras.calls.di.callModule
import com.zhigaras.calls.di.webRtcModule
import com.zhigaras.home.di.homeModule
import com.zhigaras.login.di.loginModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class App : Application() {
    
    override fun onCreate() {
        super.onCreate()
        FirebaseApp.initializeApp(this)
        Firebase.crashlytics.setCrashlyticsCollectionEnabled(false)
        startKoin {
            androidContext(this@App)
            modules(
                listOf(mainModule())
                        + loginModule()
                        + homeModule()
                        + callModule()
                        + webRtcModule()
            )
        }
    }
}
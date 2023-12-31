object Dependencies {
    
    const val koinAndroid = "io.insert-koin:koin-android:${Versions.koin}"
    
    //    const val koinCore = "io.insert-koin:koin-core:${Versions.koin}" todo remove?
    const val appcompat = "androidx.appcompat:appcompat:${Versions.appcompat}"
    const val coreKtx = "androidx.core:core-ktx:${Versions.coreKtx}"
    const val firebaseBom = "com.google.firebase:firebase-bom:${Versions.firebaseBom}"
    const val crashlytics = "com.google.firebase:firebase-crashlytics-ktx"
    const val analytics = "com.google.firebase:firebase-analytics-ktx"
    const val firebaseAuth = "com.google.firebase:firebase-auth-ktx"
    const val googlePlayAuth = "com.google.android.gms:play-services-auth:${Versions.googlePlayAuth}"
    const val firebaseDatabase = "com.google.firebase:firebase-database-ktx"
    const val material = "com.google.android.material:material:${Versions.material}"
    const val lificycle = "androidx.lifecycle:lifecycle-viewmodel-ktx:${Versions.lifecycle}"
    const val webRtc = "io.getstream:stream-webrtc-android-ui:${Versions.webRtc}"
    const val recyclerView = "androidx.recyclerview:recyclerview:${Versions.recyclerView}"
    
    const val jUnit = "junit:junit:${Versions.jUnit}"
    const val androidJUnit = "androidx.test.ext:junit:${Versions.androidJUnit}"
    const val espresso = "androidx.test.espresso:espresso-core:${Versions.espresso}"
    
    object Versions {
        
        const val koin = "3.5.0"
        const val appcompat = "1.6.1"
        const val coreKtx = "1.10.1"
        const val firebaseBom = "32.2.2"
        const val material = "1.9.0"
        const val lifecycle = "2.6.1"
        const val webRtc = "1.0.4"
        const val googlePlayAuth = "20.7.0"
        const val recyclerView = "1.3.1"
        
        const val jUnit = "4.13.2"
        const val androidJUnit = "1.1.5"
        const val espresso = "3.5.1"
    }
}
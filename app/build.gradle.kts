plugins {
    id(Plugins.application)
    id(Plugins.android)
    id(Plugins.crashlytics)
    id(Plugins.googleServices)
}

android {
    namespace = "com.zhigaras.discuss"
    compileSdk = Config.compileSdk
    
    defaultConfig {
        applicationId = "com.zhigaras.discuss"
        minSdk = Config.minSdk
        targetSdk = Config.targetSdk
        versionCode = Config.versionCode
        versionName = Config.versionName
        
        testInstrumentationRunner = Config.testInstrumentationRunner
    }
    
    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = Config.javaVersion
        targetCompatibility = Config.javaVersion
    }
    kotlinOptions {
        jvmTarget = Config.jvmTarget
    }
    buildFeatures { viewBinding = true }
}

dependencies {
    
    implementation(project(":core"))
    implementation(project(":feature:login"))
    implementation(project(":feature:home"))
    implementation(project(":feature:calls"))
    implementation(project(":feature:profile"))
    
    implementation(Dependencies.coreKtx)
    implementation(Dependencies.appcompat)
    
    implementation(Dependencies.koinAndroid)
    
    implementation(platform(Dependencies.firebaseBom))
    implementation(Dependencies.crashlytics)
    implementation(Dependencies.analytics)
    
    debugImplementation("com.squareup.leakcanary:leakcanary-android:2.12")
    
    testImplementation(Dependencies.jUnit)
    androidTestImplementation(Dependencies.androidJUnit)
    androidTestImplementation(Dependencies.espresso)
}
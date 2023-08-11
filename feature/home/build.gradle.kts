plugins {
    id(Plugins.library)
    id(Plugins.android)
}

android {
    namespace = "com.zhigaras.home"
    compileSdk = Config.compileSdk
    
    defaultConfig {
        minSdk = Config.minSdk
        
        testInstrumentationRunner = Config.testInstrumentationRunner
        consumerProguardFiles("consumer-rules.pro")
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
    
    implementation(Dependencies.coreKtx)
    implementation(Dependencies.appcompat)
    implementation(Dependencies.material)
    implementation(Dependencies.koinAndroid)
    
    testImplementation(Dependencies.jUnit)
    androidTestImplementation(Dependencies.androidJUnit)
    androidTestImplementation(Dependencies.espresso)
}
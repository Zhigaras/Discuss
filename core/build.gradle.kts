plugins {
    id(Plugins.library)
    id(Plugins.android)
}

android {
    namespace = "com.zhigaras.core"
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
    
    implementation(Dependencies.lificycle)
    implementation(Dependencies.material)
    
    implementation(Dependencies.koinAndroid)
    
}
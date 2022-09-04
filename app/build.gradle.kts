import com.android.build.gradle.internal.cxx.configure.gradleLocalProperties

plugins {
    id("com.android.application")
    id("com.google.gms.google-services")
    id("dagger.hilt.android.plugin")
    id("com.google.firebase.crashlytics")

    kotlin("android")
    kotlin("kapt")
}

val key: String = gradleLocalProperties(rootDir).getProperty("API_KEY")

android {
    compileSdk = ProjectConfig.compileSdk
    defaultConfig {
        applicationId = ProjectConfig.appId
        minSdk = ProjectConfig.minSdk
        targetSdk = ProjectConfig.targetSdk
        versionCode = ProjectConfig.versionCode
        versionName = ProjectConfig.versionName

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }

        buildConfigField("String", "API_KEY", "\"$key\"")
    }

    buildTypes {
        getByName("release") {
            isMinifyEnabled = true
            isDebuggable = false
            isShrinkResources = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
    buildFeatures {
        viewBinding = true
    }
}

dependencies {
    //TODO - Add Modules

    implementation(Core.coreKtx)
    implementation(Core.appCompat)
    implementation(Core.constraintLayout)

    implementation(Google.material)
    implementation(Google.googleAuth)

    implementation(Navigation.fragment)
    implementation(Navigation.ui)

    implementation(Retrofit.retrofit)
    implementation(Retrofit.moshiConverter)
    implementation(Retrofit.okHttp)
    implementation(Retrofit.okHttpLoggingInterceptor)

    implementation(Coroutines.core)
    implementation(Coroutines.android)
    implementation(Coroutines.taskAwait)

    implementation(Lifecycle.liveData)
    implementation(Lifecycle.viewModel)
    implementation(Lifecycle.runtime)
    implementation(Lifecycle.extension)

    implementation(Moshi.moshi)
    kapt(Moshi.codegen)

    implementation(Hilt.android)
    kapt(Hilt.compiler)

    implementation(Glide.glide)
    kapt(Glide.glideCompiler)

    implementation(platform(Firebase.platform))
    implementation(Firebase.analytics)
    implementation(Firebase.crashlytics)
    implementation(Firebase.auth)
    implementation(Firebase.messaging)

    implementation(External.timber)
    implementation(External.countdownView)
    implementation(External.circleImageView)
    implementation(External.roundedImageView)
    implementation(External.carousel)
    implementation(External.qrCode)

    implementation(Testing.junit4)
    implementation(Testing.junitAndroidExt)
}
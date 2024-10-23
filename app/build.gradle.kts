plugins {
    alias(libs.plugins.com.android.application)
    alias(libs.plugins.com.google.services)
    alias(libs.plugins.hilt.gradle)
    alias(libs.plugins.firebase.crashlytics.gradle)
    id("com.google.devtools.ksp")
    kotlin("android")
}

android {
    namespace = "com.orangeink.techtrix"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.orangeink.techtrix"
        minSdk = 23
        targetSdk = 34
        versionCode = 13
        versionName = "1.2.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
    }

    buildTypes {
        getByName("release") {
            extra["enableCrashlytics"] = true
            isMinifyEnabled = true
            isDebuggable = false
            isShrinkResources = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
        getByName("debug") {
            extra["enableCrashlytics"] = false
            isMinifyEnabled = false
            isDebuggable = true
        }
    }
    compileOptions {
        isCoreLibraryDesugaringEnabled = true
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = "17"
    }
    buildFeatures {
        viewBinding = true
        buildConfig = true
    }
}

dependencies {
    coreLibraryDesugaring(libs.corelibrary.desugaring)

    implementation(project(":core:network"))
    implementation(project(":core:common"))
    implementation(project(":core:design"))
    implementation(project(":core:utils"))

    implementation(project(":feature:home"))
    implementation(project(":feature:search"))
    implementation(project(":feature:registration"))
    implementation(project(":feature:notifications"))
    implementation(project(":feature:category"))
    implementation(project(":feature:event"))
    implementation(project(":feature:profile"))
    implementation(project(":feature:list"))
    implementation(project(":feature:login"))

    implementation(libs.core.ktx)
    implementation(libs.app.compat)
    implementation(libs.constraint.layout)
    implementation(libs.swiperefresh.layout)
    implementation(libs.splashscreen)

    implementation(libs.fragment)
    implementation(libs.google.material)

    implementation(libs.navigation.fragment)
    implementation(libs.navigation.ui)

    implementation(libs.coroutines.core)
    implementation(libs.coroutines.android)
    implementation(libs.coroutines.play.services)

    implementation(libs.lifecycle.viewmodel)
    implementation(libs.lifecycle.livedata)
    implementation(libs.lifecycle.runtime)
    implementation(libs.lifecycle.extension)

    implementation(libs.hilt.android)
    ksp(libs.hilt.compiler)

    implementation(libs.glide)
    ksp(libs.glide.compiler)

    implementation(platform(libs.firebase.platform))
    implementation(libs.firebase.analytics)
    implementation(libs.firebase.crashlytics)
    implementation(libs.firebase.auth)
    implementation(libs.firebase.messaging)

    implementation(libs.timber)

    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.test.ext.junit)
    androidTestImplementation(libs.espresso.core)
}
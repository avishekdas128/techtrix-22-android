object Core {
    private const val coreKtxVersion = "1.7.0"
    const val coreKtx = "androidx.core:core-ktx:$coreKtxVersion"

    private const val appCompatVersion = "1.5.0"
    const val appCompat = "androidx.appcompat:appcompat:$appCompatVersion"

    private const val constraintLayoutVersion = "2.1.4"
    const val constraintLayout =
        "androidx.constraintlayout:constraintlayout:$constraintLayoutVersion"

    private const val swipeRefreshLayoutVersion = "1.1.0"
    const val swipeRefresh =
        "androidx.swiperefreshlayout:swiperefreshlayout:$swipeRefreshLayoutVersion"
}

object Google {
    private const val materialVersion = "1.5.0"
    const val material = "com.google.android.material:material:$materialVersion"

    private const val googleAuthVersion = "20.2.0"
    const val googleAuth = "com.google.android.gms:play-services-auth:$googleAuthVersion"
}

object Testing {
    private const val junitVersion = "4.13.2"
    const val junit4 = "junit:junit:$junitVersion"

    private const val junitAndroidExtVersion = "1.1.3"
    const val junitAndroidExt = "androidx.test.ext:junit:$junitAndroidExtVersion"
}

object Coroutines {
    private const val coroutinesVersion = "1.6.4"

    const val core = "org.jetbrains.kotlinx:kotlinx-coroutines-core:$coroutinesVersion"
    const val android = "org.jetbrains.kotlinx:kotlinx-coroutines-android:$coroutinesVersion"

    private const val taskAwaitVersion = "1.3.1"
    const val taskAwait = "org.jetbrains.kotlinx:kotlinx-coroutines-play-services:$taskAwaitVersion"
}

object Retrofit {
    private const val version = "2.9.0"
    const val retrofit = "com.squareup.retrofit2:retrofit:$version"
    const val moshiConverter = "com.squareup.retrofit2:converter-moshi:$version"

    private const val okHttpVersion = "4.9.0"
    const val okHttp = "com.squareup.okhttp3:okhttp:$okHttpVersion"
    const val okHttpLoggingInterceptor = "com.squareup.okhttp3:logging-interceptor:$okHttpVersion"
}

object Hilt {
    private const val hiltVersion = "2.42"
    const val android = "com.google.dagger:hilt-android:$hiltVersion"
    const val compiler = "com.google.dagger:hilt-compiler:$hiltVersion"
}

object Glide {
    private const val glideVersion = "4.12.0"

    const val glide = "com.github.bumptech.glide:glide:$glideVersion"
    const val glideCompiler = "com.github.bumptech.glide:compiler:$glideVersion"
}

object Moshi {
    private const val moshiVersion = "1.13.0"

    const val moshi = "com.squareup.moshi:moshi-kotlin:$moshiVersion"
    const val codegen = "com.squareup.moshi:moshi-kotlin-codegen:$moshiVersion"
}

object Navigation {
    private const val version = "2.5.1"

    const val fragment = "androidx.navigation:navigation-fragment-ktx:$version"
    const val ui = "androidx.navigation:navigation-ui-ktx:$version"
}

object Lifecycle {
    private const val version = "2.5.1"

    const val viewModel = "androidx.lifecycle:lifecycle-viewmodel-ktx:$version"
    const val liveData = "androidx.lifecycle:lifecycle-livedata-ktx:$version"
    const val runtime = "androidx.lifecycle:lifecycle-runtime-ktx:$version"

    private const val extensionVersion = "2.2.0"
    const val extension = "androidx.lifecycle:lifecycle-extensions:$extensionVersion"
}

object Firebase {
    private const val bomVersion = "29.3.0"

    const val platform = "com.google.firebase:firebase-bom:$bomVersion"
    const val crashlytics = "com.google.firebase:firebase-crashlytics-ktx"
    const val analytics = "com.google.firebase:firebase-analytics-ktx"
    const val auth = "com.google.firebase:firebase-auth-ktx"
    const val messaging = "com.google.firebase:firebase-messaging-ktx"
}

object External {
    private const val timberVersion = "5.0.1"
    const val timber = "com.jakewharton.timber:timber:$timberVersion"

    private const val carouselVersion = "1.0.4"
    const val carousel = "com.github.akshaaatt:Sliding-Carousel:$carouselVersion"

    private const val countdownVersion = "2.1.6"
    const val countdownView = "com.github.iwgang:countdownview:$countdownVersion"

    private const val roundedImageVersion = "2.3.0"
    const val roundedImageView = "com.makeramen:roundedimageview:$roundedImageVersion"

    private const val circleImageVersion = "3.1.0"
    const val circleImageView = "de.hdodenhof:circleimageview:$circleImageVersion"

    private const val qrCodeVersion = "3.3.3"
    const val qrCode = "com.google.zxing:core:$qrCodeVersion"
}

object Kotlin {
    const val version = "1.7.10"
}
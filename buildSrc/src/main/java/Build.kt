object Build {

    private const val androidBuildToolsVersion = "7.2.2"
    const val androidBuildTools = "com.android.tools.build:gradle:$androidBuildToolsVersion"

    const val kotlinGradlePlugin = "org.jetbrains.kotlin:kotlin-gradle-plugin:${Kotlin.version}"

    private const val hiltAndroidGradlePluginVersion = "2.42"
    const val hiltAndroidGradlePlugin =
        "com.google.dagger:hilt-android-gradle-plugin:$hiltAndroidGradlePluginVersion"

    private const val googleServicesPluginVersion = "4.3.13"
    const val googleServicesGradlePlugin =
        "com.google.gms:google-services:$googleServicesPluginVersion"

    private const val crashlyticsPluginVersion = "2.9.1"
    const val crashlyticsPlugin =
        "com.google.firebase:firebase-crashlytics-gradle:$crashlyticsPluginVersion"
}
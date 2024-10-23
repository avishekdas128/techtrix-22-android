plugins {
    alias(libs.plugins.android.library)
}

apply {
    from("$rootDir/base-module.gradle")
}

android {
    namespace = "com.orangeink.network" // Only namespace is required
}


dependencies {
    "implementation"(libs.retrofit)
    "implementation"(libs.retrofit.converter)
    "implementation"(libs.okhttp)
    "implementation"(libs.okhttp.logging)

    "implementation"(libs.moshi)
    "ksp"(libs.moshi.codegen)
}
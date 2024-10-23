plugins {
    alias(libs.plugins.android.library)
}

apply {
    from("$rootDir/base-module.gradle")
}

android {
    namespace = "com.orangeink.common" // Only namespace is required
}

dependencies {
    "implementation"(project(":core:network"))
    "implementation"(project(":core:design"))

    "implementation"(platform(libs.firebase.platform))
    "implementation"(libs.firebase.auth)

    "implementation"(libs.app.compat)
    "implementation"(libs.google.material)

    "implementation"(libs.moshi)
    "ksp"(libs.moshi.codegen)
}
plugins {
    alias(libs.plugins.android.library)
}

apply {
    from("$rootDir/base-module.gradle")
}

android {
    namespace = "com.orangeink.utils" // Only namespace is required
}


dependencies {
    "implementation"(libs.glide)
    "ksp"(libs.glide.compiler)
}
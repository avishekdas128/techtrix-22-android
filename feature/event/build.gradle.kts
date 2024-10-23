plugins {
    alias(libs.plugins.android.library)
}

apply {
    from("$rootDir/base-module.gradle")
}

android {
    namespace = "com.orangeink.event" // Only namespace is required
}


dependencies {
    "implementation"(project(":core:common"))
    "implementation"(project(":core:design"))
    "implementation"(project(":core:network"))
    "implementation"(project(":core:utils"))

    "implementation"(libs.app.compat)
    "implementation"(libs.google.material)

    "implementation"(platform(libs.firebase.platform))
    "implementation"(libs.firebase.auth)

    "implementation"(libs.retrofit)
}
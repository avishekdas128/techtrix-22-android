plugins {
    alias(libs.plugins.android.library)
}

apply {
    from("$rootDir/base-module.gradle")
}

android {
    namespace = "com.orangeink.notifications" // Only namespace is required
}


dependencies {
    "implementation"(project(":core:common"))
    "implementation"(project(":core:design"))
    "implementation"(project(":core:network"))
    "implementation"(project(":core:utils"))

    "implementation"(libs.app.compat)
    "implementation"(libs.swiperefresh.layout)
    "implementation"(libs.google.material)

    "implementation"(libs.retrofit)
}
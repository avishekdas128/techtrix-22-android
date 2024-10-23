plugins {
    alias(libs.plugins.android.library)
}

apply {
    from("$rootDir/base-module.gradle")
}

android {
    namespace = "com.orangeink.login" // Only namespace is required
}


dependencies {
    "implementation"(project(":core:common"))
    "implementation"(project(":core:design"))
    "implementation"(project(":core:network"))
    "implementation"(project(":core:utils"))

    "implementation"(platform(libs.firebase.platform))
    "implementation"(libs.firebase.auth)

    "implementation"(libs.google.auth)

    "implementation"(libs.app.compat)
    "implementation"(libs.swiperefresh.layout)
    "implementation"(libs.google.material)

    "implementation"(libs.coroutines.play.services)

    "implementation"(libs.retrofit)
    "implementation"(libs.circleimage.view)
}
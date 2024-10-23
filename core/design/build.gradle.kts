plugins {
    alias(libs.plugins.android.library)
}

apply {
    from("$rootDir/base-module.gradle")
}

android {
    namespace = "com.orangeink.design" // Only namespace is required
}


dependencies {
    "implementation"(libs.constraint.layout)
    "implementation"(libs.app.compat)
    "implementation"(libs.swiperefresh.layout)
    "implementation"(libs.google.material)
}
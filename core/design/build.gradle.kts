apply {
    from("$rootDir/base-module.gradle")
}

dependencies {
    "implementation"(Core.constraintLayout)
    "implementation"(Core.appCompat)
    "implementation"(Core.swipeRefresh)
    "implementation"(Google.material)
}
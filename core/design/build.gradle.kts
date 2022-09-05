apply {
    from("$rootDir/base-module.gradle")
}

dependencies {
    "implementation"(Core.constraintLayout)
    "implementation"(Core.swipeRefresh)
    "implementation"(Google.material)
}
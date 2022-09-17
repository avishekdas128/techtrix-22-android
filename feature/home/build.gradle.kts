apply {
    from("$rootDir/base-module.gradle")
}

dependencies {
    "implementation"(project(Modules.coreCommon))
    "implementation"(project(Modules.coreDesign))
    "implementation"(project(Modules.coreNetwork))
    "implementation"(project(Modules.coreUtils))

    "implementation"(Core.appCompat)
    "implementation"(Google.material)

    "implementation"(platform(Firebase.platform))
    "implementation"(Firebase.auth)

    "implementation"(Retrofit.retrofit)

    "implementation"(External.countdownView)
    "implementation"(External.carousel)
}
apply {
    from("$rootDir/base-module.gradle")
}

dependencies {
    "implementation"(project(Modules.coreCommon))
    "implementation"(project(Modules.coreDesign))
    "implementation"(project(Modules.coreNetwork))
    "implementation"(project(Modules.coreUtils))

    "implementation"(platform(Firebase.platform))
    "implementation"(Firebase.auth)
    "implementation"(Firebase.messaging)

    "implementation"(Core.appCompat)
    "implementation"(Core.swipeRefresh)
    "implementation"(Google.material)

    "implementation"(Retrofit.retrofit)
    "implementation"(External.circleImageView)
}
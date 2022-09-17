apply {
    from("$rootDir/base-module.gradle")
}

dependencies {
    "implementation"(project(Modules.coreNetwork))
    "implementation"(project(Modules.coreDesign))

    "implementation"(platform(Firebase.platform))
    "implementation"(Firebase.auth)

    "implementation"(Core.appCompat)
    "implementation"(Google.material)

    "implementation"(Moshi.moshi)
    "kapt"(Moshi.codegen)
}
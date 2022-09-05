apply {
    from("$rootDir/base-module.gradle")
}

dependencies {
    "implementation"(Glide.glide)
    "kapt"(Glide.glideCompiler)
}
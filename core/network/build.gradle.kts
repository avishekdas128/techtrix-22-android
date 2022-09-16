apply {
    from("$rootDir/base-module.gradle")
}

dependencies {
    "implementation"(Retrofit.retrofit)
    "implementation"(Retrofit.moshiConverter)
    "implementation"(Retrofit.okHttp)
    "implementation"(Retrofit.okHttpLoggingInterceptor)

    "implementation"(Moshi.moshi)
    "kapt"(Moshi.codegen)
}
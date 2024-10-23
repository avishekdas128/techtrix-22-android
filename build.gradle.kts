// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    alias(libs.plugins.com.android.application) apply false
    alias(libs.plugins.org.jetbrains.kotlin.android) apply false
    alias(libs.plugins.com.google.services) apply false
    alias(libs.plugins.hilt.gradle) apply false
    alias(libs.plugins.firebase.crashlytics.gradle) apply false
    alias(libs.plugins.android.library) apply false
    id("com.google.devtools.ksp") version "2.0.0-1.0.24" apply false
}
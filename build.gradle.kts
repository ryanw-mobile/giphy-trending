// Top-level build file where you can add configuration options common to all sub-projects/modules.
buildscript {
    repositories {
        google()
        mavenCentral()
    }
}

plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.android.library) apply false
    alias(libs.plugins.kotlin.android) apply false
    alias(libs.plugins.hilt.android.plugin) apply false
    alias(libs.plugins.devtools.ksp) apply false
    alias(libs.plugins.kotlin.kapt) apply false
    alias(libs.plugins.androidx.navigation.safeargs)apply false
}

tasks.register("clean", Delete::class) {
    delete(rootProject.buildDir)
}
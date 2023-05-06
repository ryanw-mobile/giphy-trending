import java.io.FileInputStream
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Properties

@Suppress("DSL_SCOPE_VIOLATION")
plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.devtools.ksp)
    alias(libs.plugins.kotlin.kapt)
    alias(libs.plugins.androidx.navigation.safeargs)
    alias(libs.plugins.hilt.android.plugin)
    alias(libs.plugins.kotlinx.kover)
    alias(libs.plugins.gradle.ktlint)
}

android {
    namespace = "uk.ryanwong.giphytrending"
    compileSdk = libs.versions.compileSdk.get().toInt()

    signingConfigs {
        create("release")
    }

    val isRunningOnBitrise = System.getenv("BITRISE") == "true"

    if (isRunningOnBitrise) {
        // configure keystore
        // File Downloaderで指定したパス
        signingConfigs.getByName("release").apply {
            storeFile = file(System.getenv("HOME") + "/keystores/release.jks")
            storePassword = System.getenv("BITRISEIO_ANDROID_KEYSTORE_PASSWORD")
            keyAlias = System.getenv("BITRISEIO_ANDROID_KEYSTORE_ALIAS")
            keyPassword = System.getenv("BITRISEIO_ANDROID_KEYSTORE_PRIVATE_KEY_PASSWORD")
        }

        // Extra keys attached in the keystore.properties
        defaultConfig.buildConfigField(
            "String",
            "GIPHY_API_KEY",
            System.getenv("giphyApiKey"),
        )
    } else {
        val keyProps = Properties()
        keyProps.load(FileInputStream(file("../../keystore.properties")))
        signingConfigs.getByName("release").apply {
            storeFile = file(keyProps["store"].toString())
            keyAlias = keyProps["alias"].toString()
            storePassword = keyProps["storePass"].toString()
            keyPassword = keyProps["pass"].toString()
        }

        // Extra keys attached in the keystore.properties
        defaultConfig.buildConfigField("String", "GIPHY_API_KEY", "${keyProps["giphyApiKey"]}")
    }

    defaultConfig {
        applicationId = "uk.ryanwong.giphytrending"
        minSdk = libs.versions.minSdk.get().toInt()
        targetSdk = libs.versions.targetSdk.get().toInt()
        versionCode = 6
        versionName = "1.4.0"

        resourceConfigurations += setOf("en")

        testInstrumentationRunner = "uk.ryanwong.giphytrending.CustomTestRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
        // Bundle output filename
        val timestamp = SimpleDateFormat("yyyyMMdd-HHmmss").format(Date())
        setProperty("archivesBaseName", "giphy-$versionName-$timestamp")

        // Configurable values - We are able to set different values for each build
        buildConfigField("String", "GIPHY_ENDPOINT", "\"https://api.giphy.com/\"")
        buildConfigField("String", "DATABASE_NAME", "\"trending.db\"")
        buildConfigField("String", "API_MAX_ENTRIES", "\"100\"")
        buildConfigField("String", "API_RATING", "\"G\"")
    }

    buildTypes {
        getByName("debug") {
            isMinifyEnabled = false
            isShrinkResources = false

            signingConfig = signingConfigs.getByName("release")
            applicationVariants.all {
                val variant = this
                variant.outputs.map { it as com.android.build.gradle.internal.api.BaseVariantOutputImpl }
                    .forEach { output ->
                        val timestamp = SimpleDateFormat("yyyyMMdd-HHmmss").format(Date())
                        val outputFileName =
                            "giphy-${variant.name}-${variant.versionName}-$timestamp.apk"
                        output.outputFileName = outputFileName
                    }
            }
        }
        create("benchmark") {
            initWith(getByName("release"))
            isDebuggable = false
            isMinifyEnabled = true
            isShrinkResources = true

            signingConfig = signingConfigs.getByName("debug")
            matchingFallbacks.add("release")
        }

        getByName("release") {
            isMinifyEnabled = true
            isShrinkResources = true

            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro",
            )

            signingConfig = signingConfigs.getByName("release")
            applicationVariants.all {
                val variant = this
                variant.outputs.map { it as com.android.build.gradle.internal.api.BaseVariantOutputImpl }
                    .forEach { output ->
                        val timestamp = SimpleDateFormat("yyyyMMdd-HHmmss").format(Date())
                        val outputFileName =
                            "giphy-${variant.name}-${variant.versionName}-$timestamp.apk"
                        output.outputFileName = outputFileName
                    }
            }
        }
    }

    /**
     * Source sets can no longer contain shared roots as this is impossible to represent in the IDE.
     * In order to share sources between test and androidTest we should be able to use test fixtures.
     */
//    testFixtures {
//        enable = true
//        androidResources = true
//    }
    buildFeatures {
        dataBinding = true
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }

    sourceSets {
        named("test") {
            java.srcDirs("src/testFixtures/java")
        }
    }

    testOptions {
        animationsDisabled = true
    }

    packaging {
        resources {
            excludes += listOf(
                "META-INF/AL2.0",
                "META-INF/LGPL2.1",
                "META-INF/licenses/ASM",
            )
            pickFirsts += listOf(
                "win32-x86-64/attach_hotspot_windows.dll",
                "win32-x86/attach_hotspot_windows.dll",
            )
        }
    }
    dependenciesInfo {
        includeInApk = false
        includeInBundle = false
    }
    buildToolsVersion = "33.0.0"

    testOptions {
        unitTests {
            isIncludeAndroidResources = true
            isReturnDefaultValues = true
        }
    }
}

kotlin {
    jvmToolchain(11)
}

dependencies {
    implementation(fileTree(mapOf("dir" to "libs", "include" to listOf("*.jar"))))

    implementation(libs.androidx.core.splashscreen)
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.kotlin.reflect)
    implementation(libs.recyclerview)

    // Android Lifecycle Extensions
    implementation(libs.androidx.lifecycle.extensions)
    implementation(libs.androidx.activity.ktx)
    kapt(libs.common.java8)
    implementation(libs.androidx.lifecycle.livedata.ktx)
    implementation(libs.androidx.lifecycle.viewmodel.ktx)

    // Navigation
    implementation(libs.androidx.navigation.fragment.ktx)
    implementation(libs.androidx.navigation.navigation.ui.ktx)

    // Glide for Images
    implementation(libs.glide)

    // Retrofit 2
    implementation(libs.retrofit)
    implementation(libs.converter.moshi)
    implementation(libs.adapter.rxjava2)
    implementation(libs.logging.interceptor)

    // Moshi
    implementation(libs.moshi)
    implementation(libs.moshi.kotlin)
    implementation(libs.moshi.adapters)

    // Dagger Hilt
    implementation(libs.hilt.android)
    kapt(libs.hilt.compiler)

    // Room 2
    implementation(libs.androidx.room.runtime)
    implementation(libs.androidx.legacy.support.v4)
    implementation(libs.androidx.lifecycle.extensions)
    // optional - RxJava support for Room
    implementation(libs.androidx.room.ktx)
    ksp(libs.androidx.room.compiler)

    implementation(libs.kotlinx.coroutines.android)

    // Datastore preferences
    implementation(libs.androidx.datastore.preferences)

    implementation(libs.timber)

    // debugImplementation because LeakCanary should only run in debug builds.
    debugImplementation(libs.leakcanary.android)

    // testing
    testImplementation(libs.junit)
    testImplementation(libs.junit.jupiter.api)
    testImplementation(libs.core.ktx)
    testImplementation(libs.androidx.junit.ktx)
    testImplementation(libs.androidx.core.testing)
    testImplementation(libs.jetbrains.kotlinx.coroutines.test)

    // kotest
    testImplementation(libs.kotest.runner.junit5)
    testImplementation(libs.kotest.assertions.core)
    testImplementation(libs.kotest.property)

    androidTestImplementation(libs.kotest.assertions.core)
    androidTestImplementation(libs.jetbrains.kotlinx.coroutines.test)
    androidTestImplementation(libs.androidx.test.junit4)
    androidTestImplementation(libs.androidx.test.espresso.core)
    debugImplementation(libs.androidx.fragment.testing)

    // For instrumented tests - with Kotlin
    androidTestImplementation(libs.hilt.android.testing)
    androidTestImplementation(libs.androidx.test.rules)
}

tasks.withType<Test> {
    useJUnitPlatform()
}

koverMerged {
    enable()

    filters { // common filters for all default Kover tasks
        classes { // common class filter for all default Kover tasks
            excludes += listOf(
                "uk.ryanwong.giphytrending.data.di.*",
                "uk.ryanwong.giphytrending.di.*",
                "uk.ryanwong.giphytrending.data.source.di.*",
                "uk.ryanwong.giphytrending.databinding.*",
                "androidx.*",
                "com.bumptech.glide.*",
                "dagger.hilt.internal.aggregatedroot.codegen.*",
                "hilt_aggregated_deps.*",
                "uk.ryanwong.giphytrending.*.*MembersInjector",
                "uk.ryanwong.giphytrending.*.*Factory",
                "uk.ryanwong.giphytrending.*.*HiltModules*",
                "uk.ryanwong.giphytrending.data.source.local.*_Impl*",
                "uk.ryanwong.giphytrending.data.source.local.*Impl_Factory",
                "uk.ryanwong.giphytrending.DataBind*",
                "uk.ryanwong.giphytrending.BR",
                "uk.ryanwong.giphytrending.BuildConfig",
                "uk.ryanwong.giphytrending.Hilt*",
                "uk.ryanwong.giphytrending.*.Hilt_*",
            )
        }
    }

    xmlReport {
        onCheck.set(true)
    }

    htmlReport {
        onCheck.set(true)
    }
}

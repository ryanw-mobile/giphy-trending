import org.jlleitschuh.gradle.ktlint.reporter.ReporterType
import java.io.FileInputStream
import java.io.InputStreamReader
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Properties

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.hilt.android.plugin)
    alias(libs.plugins.kotlinx.kover)
    alias(libs.plugins.devtools.ksp)
    alias(libs.plugins.kotlin.kapt)
    alias(libs.plugins.androidx.navigation.safeargs)
    alias(libs.plugins.gradle.ktlint)
}

android {
    namespace = "uk.ryanwong.giphytrending"
    compileSdk = libs.versions.compileSdk.get().toInt()

    signingConfigs {
        create("release") {
            val isRunningOnBitrise = System.getenv("BITRISE") == "true"
            val keystorePropertiesFile = file("../../keystore.properties")

            if (isRunningOnBitrise || !keystorePropertiesFile.exists()) {
                keyAlias = System.getenv("BITRISEIO_ANDROID_KEYSTORE_ALIAS")
                keyPassword = System.getenv("BITRISEIO_ANDROID_KEYSTORE_PRIVATE_KEY_PASSWORD")
                storeFile = file(System.getenv("KEYSTORE_LOCATION"))
                storePassword = System.getenv("BITRISEIO_ANDROID_KEYSTORE_PASSWORD")
            } else {
                val properties = Properties()
                InputStreamReader(
                    FileInputStream(keystorePropertiesFile),
                    Charsets.UTF_8,
                ).use { reader ->
                    properties.load(reader)
                }

                keyAlias = properties.getProperty("alias")
                keyPassword = properties.getProperty("pass")
                storeFile = file(properties.getProperty("store"))
                storePassword = properties.getProperty("storePass")
            }
        }
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

        val isRunningOnBitrise = System.getenv("BITRISE") == "true"
        val keystorePropertiesFile = file("../../keystore.properties")
        if (isRunningOnBitrise || !keystorePropertiesFile.exists()) {
            // Extra keys attached in the keystore.properties
            defaultConfig.buildConfigField(
                "String",
                "GIPHY_API_KEY",
                System.getenv("GIPHYAPIKEY"),
            )
        } else {
            val properties = Properties()
            InputStreamReader(
                FileInputStream(keystorePropertiesFile),
                Charsets.UTF_8,
            ).use { reader ->
                properties.load(reader)
            }

            // Extra keys attached in the keystore.properties
            defaultConfig.buildConfigField(
                "String",
                "GIPHY_API_KEY",
                properties.getProperty("GIPHYAPIKEY") ?: "\"\"",
            )
        }
    }

    buildTypes {
        fun setOutputFileName() {
            applicationVariants.all {
                val variant = this
                variant.outputs
                    .map { it as com.android.build.gradle.internal.api.BaseVariantOutputImpl }
                    .forEach { output ->
                        val timestamp = SimpleDateFormat("yyyyMMdd-HHmmss").format(Date())
                        val outputFileName =
                            "giphy-${variant.name}-${variant.versionName}-$timestamp.apk"
                        output.outputFileName = outputFileName
                    }
            }
        }

        getByName("debug") {
            isMinifyEnabled = false
            isShrinkResources = false
            setOutputFileName()
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
            setOutputFileName()
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
        buildConfig = true
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
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
    buildToolsVersion = libs.versions.buildToolsVersion.get()

    testOptions {
        unitTests {
            isIncludeAndroidResources = true
            isReturnDefaultValues = true
        }
    }
}

kotlin {
    jvmToolchain(17)
}

dependencies {
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

configure<org.jlleitschuh.gradle.ktlint.KtlintExtension> {
    android.set(true)
    ignoreFailures.set(true)
    reporters {
        reporter(ReporterType.PLAIN)
        reporter(ReporterType.CHECKSTYLE)
        reporter(ReporterType.SARIF)
    }
}

tasks.named("preBuild") {
    dependsOn(tasks.named("ktlintFormat"))
}

tasks.withType<Test> {
    useJUnitPlatform()
}

koverReport {
    // common filters for all reports of all variants
    filters {
        // exclusions for reports
        excludes {
            // excludes class by fully-qualified JVM class name, wildcards '*' and '?' are available
            classes(
                listOf(
                    "uk.ryanwong.giphytrending.GiphyApplication",
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
                    "*Fragment",
                    "*Fragment\$*",
                    "*Activity",
                    "*Activity\$*",
                    "*.BuildConfig",
                    "*.DebugUtil",
                ),
            )
            // excludes all classes located in specified package and it subpackages, wildcards '*' and '?' are available
            packages(
                listOf(
                    "uk.ryanwong.giphytrending.data.di",
                    "uk.ryanwong.giphytrending.di",
                    "uk.ryanwong.giphytrending.data.source.di",
                    "uk.ryanwong.giphytrending.databinding",
                    "androidx",
                    "com.bumptech.glide",
                    "dagger.hilt.internal.aggregatedroot.codegen",
                    "hilt_aggregated_deps",
                ),
            )
        }
    }
}

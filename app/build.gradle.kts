import java.io.FileInputStream
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Properties

@Suppress("DSL_SCOPE_VIOLATION") plugins {
    id("com.android.application")
    id("kotlin-android")
    id("kotlin-kapt")
    id("androidx.navigation.safeargs")
    id("dagger.hilt.android.plugin")
    id("org.jetbrains.kotlinx.kover") version "0.6.1"
    id("org.jlleitschuh.gradle.ktlint") version "11.0.0"
}

android {
    namespace = "uk.ryanwong.giphytrending"

    compileSdk = 33

    signingConfigs {
        create("release")
    }

    val isRunningOnTravis = System.getenv("CI") == "true"
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
            "String", "GIPHY_API_KEY", "\"${System.getenv("giphyApiKey")}\""
        )

    } else if (isRunningOnTravis) {
        // configure keystore
        signingConfigs.getByName("release").apply {
            storeFile = file("../secure.keystore")
            storePassword = System.getenv("storePass")
            keyAlias = System.getenv("alias")
            keyPassword = System.getenv("pass")
        }

        // Extra keys attached in the keystore.properties
        defaultConfig.buildConfigField(
            "String", "GIPHY_API_KEY", "\"${System.getenv("giphyApiKey")}\""
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
        defaultConfig.buildConfigField("String", "GIPHY_API_KEY", "\"${keyProps["giphyApiKey"]}\"")
    }

    defaultConfig {
        applicationId = "uk.ryanwong.giphytrending"
        minSdk = 21
        targetSdk = 32
        versionCode = 5
        versionName = "1.4.0"

        resourceConfigurations += setOf("en")

        testInstrumentationRunner = "uk.ryanwong.giphytrending.CustomTestRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
        // Bundle output filename
        val timestamp = SimpleDateFormat("yyyyMMdd-HHmmss").format(Date())
        setProperty("archivesBaseName", "giphy-" + versionName + timestamp)

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
//        getByName("benchmark") {
//            isDebuggable = false
//            isMinifyEnabled = true
//            isShrinkResources = true
//            matchingFallbacks.add("release")
//
//            signingConfig = signingConfigs.getByName("debug")
//        }
        getByName("release") {
            isMinifyEnabled = true
            isShrinkResources = true

            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro"
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

    kotlinOptions {
        jvmTarget = "11"
        freeCompilerArgs += listOf("-Xjvm-default=all")
    }

    sourceSets {
        named("test") {
            java.srcDirs("src/testFixtures/java")
        }
    }

    testOptions {
        animationsDisabled = true
    }

    packagingOptions {
        resources {
            excludes += listOf(
                "META-INF/AL2.0", "META-INF/LGPL2.1", "META-INF/licenses/ASM"
            )
            pickFirsts += listOf(
                "win32-x86-64/attach_hotspot_windows.dll", "win32-x86/attach_hotspot_windows.dll"
            )
        }
    }

    testOptions {
        unitTests {
            isIncludeAndroidResources = true
            isReturnDefaultValues = true
        }
    }
}

dependencies {
    implementation("androidx.core:core-splashscreen:1.0.0")
    implementation("androidx.core:core-ktx:1.9.0")
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.8.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    implementation("org.jetbrains.kotlin:kotlin-reflect:1.8.10")

    val retrofitVersion = "2.9.0"
    val roomVersion = "2.5.0"

    implementation(fileTree(mapOf("dir" to "libs", "include" to listOf("*.jar"))))
    implementation("androidx.recyclerview:recyclerview:1.2.1")

    // Android Lifecycle Extensions
    val lifecycleVersion = "2.5.1"
    implementation("androidx.lifecycle:lifecycle-extensions:2.2.0")
    implementation("androidx.activity:activity-ktx:1.6.1")
    kapt("android.arch.lifecycle:common-java8:1.1.1")
    implementation("androidx.lifecycle:lifecycle-livedata-ktx:$lifecycleVersion")
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:$lifecycleVersion")

    // Navigation
    val navigationVersion = "2.3.5"
    implementation("androidx.navigation:navigation-fragment-ktx:2.5.3")
    implementation("androidx.navigation:navigation-ui-ktx:2.5.3")

    // Glide for Images
    implementation("com.github.bumptech.glide:glide:4.15.0")
    kapt("com.github.bumptech.glide:compiler:4.15.0")

    // Retrofit 2
    implementation("com.squareup.retrofit2:retrofit:$retrofitVersion")
    implementation("com.squareup.retrofit2:converter-moshi:2.9.0")
    implementation("com.squareup.retrofit2:adapter-rxjava2:$retrofitVersion")
    implementation("com.squareup.okhttp3:logging-interceptor:5.0.0-alpha.11")

    // Moshi
    val moshi_version = "1.14.0"
    implementation("com.squareup.moshi:moshi:$moshi_version")
    implementation("com.squareup.moshi:moshi-kotlin:$moshi_version")
    implementation("com.squareup.moshi:moshi-adapters:$moshi_version")

    // Dagger Hilt
    implementation("com.google.dagger:hilt-android:2.45")
    kapt("com.google.dagger:hilt-compiler:2.45")

    // Room 2
    val room_version = "2.5.0"
    implementation("androidx.room:room-runtime:$room_version")
    implementation("androidx.legacy:legacy-support-v4:1.0.0")
    implementation("androidx.lifecycle:lifecycle-extensions:2.2.0")
    // optional - RxJava support for Room
    implementation("androidx.room:room-ktx:$room_version")
    kapt("androidx.room:room-compiler:$room_version")

    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.6.4")

    // Datastore preferences
    val datastore_version = "1.0.0"
    implementation("androidx.datastore:datastore-preferences:$datastore_version")

    implementation("com.jakewharton.timber:timber:5.0.1")

    // debugImplementation because LeakCanary should only run in debug builds.
    debugImplementation("com.squareup.leakcanary:leakcanary-android:2.10")

    // testing
    testImplementation("junit:junit:4.13.2")
    testImplementation("org.junit.jupiter:junit-jupiter-api:5.9.2")
    testImplementation("androidx.test:core-ktx:1.5.0")
    testImplementation("androidx.test.ext:junit-ktx:1.1.5")
    testImplementation("androidx.arch.core:core-testing:2.2.0")
    testImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:1.6.4")

    // kotest
    testImplementation("io.kotest:kotest-runner-junit5:5.5.5")
    testImplementation("io.kotest:kotest-assertions-core:5.5.5")
    testImplementation("io.kotest:kotest-property:5.5.5")

    androidTestImplementation("io.kotest:kotest-assertions-core:5.5.5")
    androidTestImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:1.6.4")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
    debugImplementation("androidx.fragment:fragment-testing:1.5.5")

    // For instrumented tests - with Kotlin
    androidTestImplementation("com.google.dagger:hilt-android-testing:2.45")
    androidTestImplementation("androidx.test:rules:1.5.0")
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
                "uk.ryanwong.giphytrending.*.Hilt_*"
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

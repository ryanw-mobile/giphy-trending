# Giphy Trending ![Gradle Build](https://github.com/ryanw-mobile/giphy-trending/actions/workflows/main_build.yml/badge.svg) [![Codacy Coverage Badge](https://app.codacy.com/project/badge/Coverage/b229ea2d0be943cb8c4dde25b5d91ae2)](https://app.codacy.com/gh/ryanw-mobile/giphy-trending/dashboard?utm_source=gh&utm_medium=referral&utm_content=&utm_campaign=Badge_coverage) [![Codacy Quality Badge](https://app.codacy.com/project/badge/Grade/b229ea2d0be943cb8c4dde25b5d91ae2)](https://app.codacy.com/gh/ryanw-mobile/giphy-trending/dashboard?utm_source=gh&utm_medium=referral&utm_content=&utm_campaign=Badge_grade) [![Renovate enabled](https://img.shields.io/badge/renovate-enabled-brightgreen.svg)](https://renovatebot.com/)

This sample Android app presents the Giphy trending animated gifs for sharing or download. You can
now also search animated gifs by keyword. It was elaborated from the work
by [Fahri Can](https://medium.com/codex/android-tutorial-part-1-using-room-with-rxjava-2-dagger-2-kotlin-and-mvvm-f8a54f77d3fa)
in Nov 2021. I migrated the codebase from RxJava to Coroutines, and then from XML Views to Jetpack
Compose. The current release is completely different from the original version after these years.

&nbsp;

### The original XML View version

The XML View version is no longer being maintained. You can check out
the [XML View version branch](https://github.com/ryanw-mobile/giphy-trending/tree/XmlView) for that.
It was using XML with Data-binding, which we
generally believe it to be a bad coding practice by tightly coupling the business logic with the UI.

&nbsp;

<p align="center">
  <img src="screenshots/240422_animated_screenshot.gif" width="600" /><br/>
  <img src="screenshots/240422_github_social_preview.png" width="600" />
</p>

&nbsp;

## Binaries download

If you want to try out the app without building it, check out
the [Releases section](https://github.com/ryanw-mobile/giphy-trending/releases) where you can find
the APK and App Bundles for each major version. A working Giphy API key was applied when building
the app, therefore you can test it by just installing it.

&nbsp;

## To-do lists

Planned enhancements are
now [logged as issues](https://github.com/ryanw-mobile/giphy-trending/issues?q=is%3Aopen+is%3Aissue+label%3Arefactor%2Cfeature%2Cfix%2Ctest).

&nbsp;

## High level architecture

* Kotlin
* MVVM & clean architecture
* Jetpack Compose - Single Activity
* Kotlin Coroutines and Flow
* Dependency Injection using Dagger Hilt
* Material 3 dynamic colour theming supporting light and dark modes
* Dynamic screen layout support using Windows Size Class
* Gradle Kotlin DSL and Version Catalog
* Macrobenchmark and Baseline Profile
* Full unit test and UI (Journey) test suite

&nbsp;

## Major libraries used

### Dependencies

* [AndroidX Core KTX](https://developer.android.com/jetpack/androidx/releases/core) - Apache 2.0 - Kotlin extensions for core Android APIs
* [JUnit](https://junit.org/junit4/) - EPL-1.0 - A simple framework to write repeatable tests
* [AndroidX Espresso](https://developer.android.com/training/testing/espresso) - Apache 2.0 - UI testing framework for Android
* [AndroidX Activity Compose](https://developer.android.com/jetpack/androidx/releases/activity) - Apache 2.0 - Compose integration with Activity
* [Jetpack Compose BOM](https://developer.android.com/jetpack/compose/bom) - Apache 2.0 - BOM for consistent Jetpack Compose dependencies
* [Jetpack Compose UI](https://developer.android.com/jetpack/compose) - Apache 2.0 - Core UI components for Compose
* [Material3](https://developer.android.com/jetpack/androidx/releases/compose-material3) - Apache 2.0 - Material Design 3 components for Compose
* [AndroidX Benchmark](https://developer.android.com/studio/profile/benchmark) - Apache 2.0 - Benchmarking tools for performance analysis
* [AndroidX Splashscreen](https://developer.android.com/jetpack/androidx/releases/core) - Apache 2.0 - Splash screen API
* [AndroidX DataStore](https://developer.android.com/topic/libraries/architecture/datastore) - Apache 2.0 - Data storage solution
* [AndroidX Legacy Support](https://developer.android.com/jetpack/androidx/releases/legacy) - Apache 2.0 - Legacy support library
* [AndroidX Lifecycle](https://developer.android.com/jetpack/androidx/releases/lifecycle) - Apache 2.0 - Lifecycle-aware components
* [AndroidX Room](https://developer.android.com/jetpack/androidx/releases/room) - Apache 2.0 - SQLite abstraction with LiveData and Coroutines support
* [Kotlinx DateTime](https://github.com/Kotlin/kotlinx-datetime) - Apache 2.0 - Date and time utilities for Kotlin
* [Timber](https://github.com/JakeWharton/timber) - Apache 2.0 - A logger with a small, extensible API
* [Coil](https://coil-kt.github.io/coil/) - Apache 2.0 - Image loading for Android backed by Kotlin Coroutines
* [MockK](https://mockk.io/) - Apache 2.0 - Mocking library for Kotlin
* [Kotlinx Coroutines](https://github.com/Kotlin/kotlinx.coroutines) - Apache 2.0 - Support for asynchronous programming in Kotlin
* [Robolectric](http://robolectric.org/) - Apache 2.0 - Unit test Android apps on the JVM
* [AndroidX Test](https://developer.android.com/testing) - Apache 2.0 - Tools for Android testing including JUnit extensions and rules
* [Hilt](https://dagger.dev/hilt/) - Apache 2.0 - Dependency injection framework built on top of Dagger
* [LeakCanary](https://square.github.io/leakcanary/) - Apache 2.0 - Memory leak detection library for Android
* [Ktor](https://ktor.io/) - Apache 2.0 - Asynchronous networking library for Kotlin

### Plugins

* [Android Application Plugin](https://developer.android.com/studio/build/gradle-plugin-3-0-0-migration) - Google - Plugin for building Android applications
* [Kotlin Android Plugin](https://kotlinlang.org/docs/gradle.html) - JetBrains - Plugin for Kotlin Android support
* [Compose Compiler Plugin](https://developer.android.com/jetpack/compose) - JetBrains - Compiler plugin for Jetpack Compose
* [Hilt Android Plugin](https://dagger.dev/hilt/gradle-setup.html) - Google - Plugin for Hilt dependency injection
* [Kover Plugin](https://github.com/Kotlin/kotlinx-kover) - JetBrains - Code coverage tool for Kotlin
* [KSP Plugin](https://github.com/google/ksp) - Google - Kotlin Symbol Processing API
* [Android Test Plugin](https://developer.android.com/studio/test) - Google - Plugin for setting up Android test modules
* [Baseline Profile Plugin](https://developer.android.com/topic/performance/baselineprofiles) - AndroidX - Plugin for generating performance profiles
* [Serialization Plugin](https://github.com/Kotlin/kotlinx.serialization) - JetBrains - Plugin for Kotlin serialization
* [Detekt Plugin](https://detekt.dev/) - Artur Bosch - Static code analysis tool for Kotlin
* [Kotlinter Plugin](https://github.com/jeremymailen/kotlinter-gradle) - Jeremy Lichtenstein - Kotlin linter for Gradle

&nbsp;

## Building the App
### Requirements

* To build the app by yourself, you need your own [Giphy API Key](https://developers.giphy.com/)

&nbsp;

### Setting up the keystore

Release builds will be signed if either the keystore file or environment variables are set.
Otherwise, the app will be built unsigned and without the Giphy API key installed, which will not
pull any data from the endpoint.

### Local

* Android Keystore is not being stored in this repository. You need your own Keystore to generate
  the apk / App Bundle

* If your project folder is at `/app/giphy-trending/`, the Keystore file and `keystore.properties`
  should be placed at `/app/`

* The format of `keystore.properties` is:
  ```
     store=/app/release-key.keystore
     alias=<alias>
     pass=<alias password>
     storePass=<keystore password>
     giphyApiKey="<your API Key here>"
  ```

### CI environment

* This project has been configured to support automated CI builds.

* The following environment variables have been set to provide the keystore:
  ```
     CI = true
     HOME = <the home directory of the bitrise environment>
     CI_ANDROID_KEYSTORE_PASSWORD = <your keystore password>
     CI_ANDROID_KEYSTORE_ALIAS = <your keystore alias>
     CI_ANDROID_KEYSTORE_PRIVATE_KEY_PASSWORD = <your keystore private key password>
     GIPHYAPIKEY= <your API Key>
  ```

### Build and install on the connected device

This app has two build variants: `Debug` and `Release`. The most common build commands are:

* `./gradlew clean installDebug`
* `./gradlew clean instal`
* `./gradlew clean bundleRelease`
* `./gradlew clean assembleRelease`

The generated apk(s) will be stored under `app/build/outputs/`

&nbsp;

## License - No Google Play / App Store submission is allowed

#### Attribution-NonCommercial (CC BY-NC 4.0)

Nothing is free in this world. RW MobiMedia UK Limited, a fully tax-paying entity in the UK,
sponsors the infrastructure and development costs of this project.

Copyright 2024 RW MobiMedia UK Limited

This project is licensed under the Creative Commons Attribution-NonCommercial 4.0 International
License. To view a copy of this license,
visit [http://creativecommons.org/licenses/by-nc/4.0/](http://creativecommons.org/licenses/by-nc/4.0/)
or send a letter to Creative Commons, PO Box 1866, Mountain View, CA 94042, USA.

#### Conditions

Under the terms of this license, you are free to:

- **Share** — copy and redistribute the material in any medium or format.
- **Adapt** — remix, transform, and build upon the material.

The licensing terms include the following stipulations:

- **Attribution** — You must give appropriate credit, provide a link to the license, and indicate if
  changes were made. You may do so in any reasonable manner, but not in any way that suggests the
  licensor endorses you or your use.
- **NonCommercial** — You may not use the material for commercial purposes. Any commercial use is
  strictly prohibited without prior permission from RW MobiMedia UK Limited.

#### Disclaimer

This work is provided "as is" without any warranties, and the licensing coverage is only applicable
as long as the distribution or use is for non-commercial purposes.

#### Further Enquiries

For permissions beyond the scope of this license, please contact RW MobiMedia UK Limited.

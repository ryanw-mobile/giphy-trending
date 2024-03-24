# giphy-trending - Sample Android App ![Gradle Build](https://github.com/ryanw-mobile/giphy-trending/actions/workflows/main_build.yml/badge.svg) [![codecov](https://codecov.io/gh/ryanw-mobile/giphy-trending/graph/badge.svg?token=J8PHIH3OPU)](https://codecov.io/gh/ryanw-mobile/giphy-trending)

This sample app was elaborated from the work
by [Fahri Can](https://medium.com/codex/android-tutorial-part-1-using-room-with-rxjava-2-dagger-2-kotlin-and-mvvm-f8a54f77d3fa)
in Nov 2021. I migrated RxJava to Coroutines, and now it is in the process of migrating the XML
Views to Compose.

![Screenshot1](screenshots/screen0.png) ![Screenshot2](screenshots/screen1.png)
![Screenshot3](screenshots/screen2.png) ![Screenshot4](screenshots/screen3.png)

## Skills covered:

This is a typical long-lasting app that the codebase survived a few migrations as new technologies,
paradigms and libraries evolved. The current target is to migrate this App to Compose, and apply a
better MVVM with Clean Architecture system design.

### The original XML View version

The XML View version is no longer being maintained. You can check out
the [XML View version branch](https://github.com/ryanw-mobile/giphy-trending/tree/XmlView) for that.
It was using XML with Data-binding, which we
generally believe it to be a bad coding practice by tightly coupling the business logic with the UI.

### High level architecture

* Kotlin
* MVVM architecture
* [`Jetpack Databinding`](https://developer.android.com/jetpack/androidx/releases/databinding)
* Kotlin Flow
* Material 3 with light and dark mode theming
* Gradle Kotlin DSL and Version Catalog

### Major libraries used

* [`Jetpack ConstraintLayout`](https://developer.android.com/jetpack/androidx/releases/constraintlayout)
* [`Jetpack Navigation`](https://developer.android.com/jetpack/androidx/releases/navigation)
* [`Jetpack Lifecycle`](https://developer.android.com/jetpack/androidx/releases/lifecycle)
* [`Jetpack PreferencesDataStore`](https://developer.android.com/jetpack/androidx/releases/datastore)
* [`Kotlin Coroutines`](https://github.com/Kotlin/kotlinx.coroutines)
* [`Kotlin Flow`](https://kotlinlang.org/docs/flow.html)
* [`Jetpack Room`](https://developer.android.com/jetpack/androidx/releases/room) - Database
* [`Retrofit2`](https://square.github.io/retrofit/)
* [`Moshi`](https://github.com/square/moshi)
* Splash Screen API
* [`Glide`](https://github.com/bumptech/glide) - Network images
* [`Dagger Hilt`](https://dagger.dev/hilt/) - DI
* [`Timber`](https://github.com/JakeWharton/timber) - Logging
* [`LeakCanary`](https://github.com/square/leakcanary) - Memory leak detection
* [`JUnit 4`](https://github.com/junit-team/junit4) - Tests
* [`kotest`](https://kotest.io/) - Tests
* [Kover](https://github.com/Kotlin/kotlinx-kover) - code coverage
* [codecov](https://codecov.io/) - code coverage
* [Github Action](https://github.com/features/actions) - CI (current)
* [Bitrise](https://app.bitrise.io/) - CI (previously)
* [Travis-CI](https://travis-ci.org/) - CI (previously)
* [Ktlint Gradle](https://github.com/jlleitschuh/ktlint-gradle) - ktlint plugin to check and apply
  code autoformat

## To-do lists:

Planned enhancements are
now [logged as issues](https://github.com/ryanw-mobile/giphy-trending/issues?q=is%3Aopen+is%3Aissue+label%3Arefactor%2Cfeature%2Cfix%2Ctest).

## Requirements

* Android Studio Iguana | 2023.2.1
* Android device or simulator running Android 9+ (API 28)

## Binaries download

If you want to try out the app without building it, check out
the [Releases section](https://github.com/ryanw-mobile/giphy-trending/releases) where you can find
the APK and App Bundles for each major version. A working Giphy API key was applied when building
the app, therefore you can test it by just installing it.

## Building the App

* To build the app by yourself, you need your own [Giphy API Key](https://developers.giphy.com/)

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
     BITRISE = true
     HOME = <the home directory of the bitrise environment>
     BITRISEIO_ANDROID_KEYSTORE_PASSWORD = <your keystore password>
     BITRISEIO_ANDROID_KEYSTORE_ALIAS = <your keystore alias>
     BITRISEIO_ANDROID_KEYSTORE_PRIVATE_KEY_PASSWORD = <your keystore private key password>
     GIPHYAPIKEY= <your API Key>
  ```

### Build and install on the connected device

This app has two build variants: `Debug` and `Release`. The most common build commands are:

* `./gradlew clean installDebug`
* `./gradlew clean instal`
* `./gradlew clean bundleRelease`
* `./gradlew clean assembleRelease`

* The generated apk(s) will be stored under `app/build/outputs/`

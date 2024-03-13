# giphy-trending - Sample Android App for skills demonstration ![Gradle Build](https://github.com/ryanw-mobile/giphy-trending/actions/workflows/main_build.yml/badge.svg) [![codecov](https://codecov.io/gh/ryanw-mobile/giphy-trending/graph/badge.svg?token=J8PHIH3OPU)](https://codecov.io/gh/ryanw-mobile/giphy-trending)

This is a sample app based on
the [walk through](https://medium.com/codex/android-tutorial-part-1-using-room-with-rxjava-2-dagger-2-kotlin-and-mvvm-f8a54f77d3fa)
by Fahri Can. As I worked on the project, the App now has most of the code rewritten on my own.

![Screenshot1](screenshots/screen0.png) ![Screenshot2](screenshots/screen1.png)
![Screenshot3](screenshots/screen2.png) ![Screenshot4](screenshots/screen3.png)

First created in Nov 2021, looking at what I have done now, I found the quality of this App
unacceptable, so I have updated again in Jul 2022.

## Skills covered:

The Android Development world is currently experiencing a shift of technology stack. Besides
migrating from Java to Kotlin, we have a choice of Coroutines over RxJava, Hilt over Dagger, and
JetPack Compose over the traditional XML View layouts. LiveData got replaced by Kotlin Flow quickly
this year.

This Sample App is for demonstrating the traditional approach which applies XML
UI, `Kotlin Coroutines`, `Kotlin Flow`,
`Dagger Hilt`, `PreferencesDataStore`.

Previously this App used `RxJava`, but it is now rewritten using `Coroutines` because it is much
more simpler.

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

## Improvements:

The original sample codes were over simplified. Modifications have been made to make the App look
more production-ready.

* The UI architecture and list item layout have been redesigned to apply Material 3 specifications
* The database schema and DAOs have been improved to support more functionalities
* The RecyclerView has been modified to use ListAdapter, DiffUtils to avoid expensive
  notifyDataSetChanged()
* Additional handling done to preserve the scrolling state and avoid flickering during refresh
* Menu button has been added to allow manual refresh
* A dedicated Domain Model was added to separate the Network Data Model
* Introduced copy image link, and open Giphy page on browser functions
* Added user customisable API limit
* Added test cases - currently there are 27 unit tests and 7 instrumented tests

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
*

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

* This project has been configured to build automatically on CI.

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

   ```
   ./gradlew installDebug
   // or
   // ./gradlew installRelease
   ```

* Options are: `Debug`, `Release`
* Debug builds will have an App package name suffix `.debug`

### Build and sign a bundle for distribution

After August 2021, all new apps and games will be required to publish with the Android App Bundle
format.

   ```
   ./gradlew clean bundleRelease
   ```

### Build and sign an apk for distribution

   ```
   ./gradlew clean assembleRelease
   ```

* The generated apk(s) will be stored under `app/build/outputs/apk/`

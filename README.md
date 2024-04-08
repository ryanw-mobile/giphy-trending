# Giphy Trending - Sample Android App ![Gradle Build](https://github.com/ryanw-mobile/giphy-trending/actions/workflows/main_build.yml/badge.svg) [![codecov](https://codecov.io/gh/ryanw-mobile/giphy-trending/graph/badge.svg?token=J8PHIH3OPU)](https://codecov.io/gh/ryanw-mobile/giphy-trending)

This sample app was elaborated from the work
by [Fahri Can](https://medium.com/codex/android-tutorial-part-1-using-room-with-rxjava-2-dagger-2-kotlin-and-mvvm-f8a54f77d3fa)
in Nov 2021. I migrated RxJava to Coroutines, and now it is in the process of migrating the XML
Views to Compose.

### The original XML View version

The XML View version is no longer being maintained. You can check out
the [XML View version branch](https://github.com/ryanw-mobile/giphy-trending/tree/XmlView) for that.
It was using XML with Data-binding, which we
generally believe it to be a bad coding practice by tightly coupling the business logic with the UI.

&nbsp;

![Screenshot1](screenshots/screen0.png) ![Screenshot2](screenshots/screen1.png)
![Screenshot3](screenshots/screen2.png) ![Screenshot4](screenshots/screen3.png)

## Skills covered:

This is a typical long-lasting app that the codebase survived a few migrations as new technologies,
paradigms and libraries evolved. The current target is to migrate this App to Compose, and apply a
better MVVM with Clean Architecture system design.

&nbsp;

### High level architecture

* Kotlin
* MVVM & clean architecture
* Dependency Injection using Dagger Hilt
* Jetpack Compose - Single Activity
* Kotlin Coroutines and Flow
* Material 3 dynamic colour theming supporting light and dark modes
* Gradle Kotlin DSL and Version Catalog

&nbsp;

### Major libraries used

* [Jetpack Compose](https://developer.android.com/jetpack/androidx/releases/compose) - Modern
  toolkit for building native UI
* [Jetpack Navigation for Compose](https://developer.android.com/jetpack/androidx/releases/navigation#navigation-compose) -
  Navigation library for Jetpack Compose applications
* [Jetpack Lifecycle](https://developer.android.com/jetpack/androidx/releases/lifecycle) -
  Lifecycle-aware components, including ViewModel support for Jetpack Compose
* [Jetpack PreferencesDataStore](https://developer.android.com/jetpack/androidx/releases/datastore) -
  Data storage solution
* [Kotlin Coroutines](https://github.com/Kotlin/kotlinx.coroutines) - Asynchronous programming
  with coroutines
* [Kotlin Flow](https://kotlinlang.org/docs/flow.html) - Reactive streams for Kotlin
* [Jetpack Room](https://developer.android.com/jetpack/androidx/releases/room) - Database library
  for local data storage
* [Retrofit2](https://square.github.io/retrofit/) - HTTP client for Android and Java
* [Moshi](https://github.com/square/moshi) - Modern JSON library for Android and Java
* [Splash Screen API](https://developer.android.com/guide/topics/ui/splash-screen) - Official
  splash screen solution
* [Coil](https://coil-kt.github.io/coil/) - Image loading library for Android, leveraging Kotlin
  Coroutines
* [Dagger Hilt](https://dagger.dev/hilt/) - Dependency injection framework
* [Timber](https://github.com/JakeWharton/timber) - Logging utility
* [LeakCanary](https://github.com/square/leakcanary) - Memory leak detection tool
* `JUnit 4` - Testing framework for Java
* [kotest](https://kotest.io/) - Powerful, flexible testing library for Kotlin
* [MockK](https://mockk.io/) - Mocking library for Kotlin
* [Ktlint Gradle](https://github.com/jlleitschuh/ktlint-gradle) - Plugin for linting and formatting
  Kotlin code
* [Kover](https://github.com/Kotlin/kotlinx-kover) - Kotlin code coverage tool
* [codecov](https://codecov.io/) - code coverage
* [Github Action](https://github.com/features/actions) - CI (current)

&nbsp;

## To-do lists:

Planned enhancements are
now [logged as issues](https://github.com/ryanw-mobile/giphy-trending/issues?q=is%3Aopen+is%3Aissue+label%3Arefactor%2Cfeature%2Cfix%2Ctest).

&nbsp;

## Requirements

* Android Studio Iguana | 2023.2.1
* Android device or simulator running Android 9+ (API 28)

&nbsp;

## Binaries download

If you want to try out the app without building it, check out
the [Releases section](https://github.com/ryanw-mobile/giphy-trending/releases) where you can find
the APK and App Bundles for each major version. A working Giphy API key was applied when building
the app, therefore you can test it by just installing it.

&nbsp;

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

The generated apk(s) will be stored under `app/build/outputs/`

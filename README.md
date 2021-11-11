# giphy-trending

This is a sample app based on
the [walk through](https://medium.com/codex/android-tutorial-part-1-using-room-with-rxjava-2-dagger-2-kotlin-and-mvvm-f8a54f77d3fa)
by Fahri Can. I have made significant modifications to the App design and architecture, therefore it
is much more than a clone.

## Skills covered:

The Android Development world is currently experiencing a shift of technology stack. Besides
migrating from Java to Kotlin, we have a choice of Coroutines over RxJava, Hilt over Dagger, and
JetPack Compose over the traditional XML/RecyclerView layouts.

This Sample App is for demonstrating the traditional approach which applies XML UI, RxJava, and
Dagger. Personally I would recommend Coroutines over RxJava, as it is much easier for coding common
REST API apps -- If we can have something done in a simple way, why make it complex?

* Kotlin
* ViewModel / MVVM architecture
* Material 3 with light and dark mode theming
* Data Binding
* Live Data
* Retrofit 2
* RxJava 2
* Glide
* Navigation
* Dagger 2

## Improvements:

The original sample codes were over simplified. Modifications have been made to make the App look
more production-ready.

* The UI architecture and list item layout have been redesigned
* The database schema and DAOs have been improved to support more functionalities
* The RecyclerView has been modified to use ListAdapter, DiffUtils to avoid expensive
  notifyDataSetChanged()
* Additional handling done to preserve the scrolling state and avoid flickering during refresh
* SwipeRefreshLayout has been added to allow manual refresh
* A dedicated Domain Model was added to separate the Network Data Model

## Requirements

* Android Studio Arctic Fox 2021.3.1 Patch 2
* Android device or simulator running Android 5.0+ (API 21)

## Setting up the keystore

* Android keystore is not being stored in this repository. You need your own keystore to generate
  the apk / App Bundle

* You also need to have your own [Giphy API Key](https://developers.giphy.com/)

* To ensure sensitive data are not being pushed to Git by accident, the keystore and its passwords
  are kept one level up of the project folder, so they are not managed by Git.

* If your project folder is at `/app/giphy-trending/`, the keystore file and `keystore.properties`
  should be placed at `/app/`

* The format of `keystore.properties` is:
  ```
     store=/app/release-key.keystore
     alias=<alias>
     pass=<alias password>
     storePass=<keystore password>
     giphyApiKey="<your API Key here>"
  ```

## Building the App

### Build and install on the connected device

   ```
   ./gradlew installDebug
   // or
   // ./gradlew installRelease
   ```

* Options are: `Debug`, `Release`
* Debug builds will have an App package name suffix `.debug`

### Build and sign an apk for distribution

   ```
   ./gradlew clean && ./gradlew assembleRelease
   ```

* The generated apk(s) will be stored under `app/build/outputs/apk/`
* Other usages can be listed using `./gradelew tasks`

## To-do lists:

This sample App is for demonstrating my coding habit and skills for potential employers. Here is a
list of things I may further work on, while waiting for my next Android Developer role:

* Tests: unit tests, instrumented tests
* Lazy loading / pagination
* Splash Screen using the new official API
* Bottom Navigation with additional fragments loading more APIs
* Photo detail view
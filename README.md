# giphy-trending

This is a sample app based on
the [walkthrough](https://medium.com/codex/android-tutorial-part-1-using-room-with-rxjava-2-dagger-2-kotlin-and-mvvm-f8a54f77d3fa)
by Fahri Can.

## Skills covered:

The Android Development world is currently experiencing a shift of technology stack. Besides
migrating from Java to Kotlin, we have a choice of Coroutines over RxJava, Hilt over Dagger, and
JetPack Compose over the traditional XML/RecyclerView layouts.

This Sample App is for demonstrating the traditional approach which applies XML UI, RxJava, and
Dagger. Personally I would recommend Coroutines over RxJava, as it is much easier for coding
standard REST API apps.

* Kotlin
* ViewModel / MVVM architecture
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

## To-do lists:

This sample App is for demonstrating my coding habit and skills for potential employers. Here is a
list of things I may further work on, while waiting for my next Android Developer role:

* Tests: unit tests, instrumented tests
* Lazy loading / pagination
* Splash Screen using the new official API
* Bottom Navigation with additional fragments loading more APIs
* Photo detail view
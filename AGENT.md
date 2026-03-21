# AI Agent Instructions

## Project Overview
**Giphy Trending** is a modern Android application that displays trending animated GIFs from Giphy. It supports searching GIFs by keyword, sharing, and downloading them. The project serves as a showcase for current Android development best practices.

- **Primary Language:** Kotlin
- **UI Framework:** Jetpack Compose (Material 3)
- **Architecture:** Clean Architecture + MVVM
- **Dependency Injection:** Dagger Hilt
- **Asynchronous Programming:** Kotlin Coroutines and Flow
- **Networking:** Ktor
- **Persistence:** Room (Database) and DataStore (Preferences)
- **Image Loading:** Coil (with Ktor and GIF support)
- **Quality & Performance:** Detekt, Kotlinter, JaCoCo, Macrobenchmark, and Baseline Profiles.

## Module Structure
- `app`: Main application module containing core logic and UI.
- `benchmark`: Macrobenchmark module for performance testing.
- `baselineprofile`: Module for generating Baseline Profiles to optimize app startup and performance.

## Key Technologies & Libraries
- **Compose Material 3:** Modern UI with dynamic color support and adaptive layouts using Window Size Classes.
- **Ktor:** Used for network requests, providing a multiplatform-friendly alternative to Retrofit.
- **Room:** SQLite abstraction for local caching of GIF data.
- **DataStore:** Type-safe replacement for SharedPreferences.
- **Timber:** Logging utility.
- **LeakCanary:** Memory leak detection in debug builds.
- **MockK & Robolectric:** Testing frameworks for JVM and Android environment simulation.

## Building and Running
### Prerequisites
- **Giphy API Key:** Required to fetch data. Get one at [Giphy Developers](https://developers.giphy.com/).
- **Keystore Configuration:** Place a `keystore.properties` file in the root directory with the following format (or set environment variables for CI):
  ```properties
  store=/path/to/release-key.keystore
  alias=your_alias
  pass=your_password
  storePass=your_keystore_password
  giphyApiKey="your_api_key"
  ```

### Key Commands
- **Install Debug Build:** `./gradlew clean installDebug`
- **Run Unit Tests:** `./gradlew test`
- **Run Instrumented Tests:** `./gradlew connectedAndroidTest`
- **Managed Device Tests:** `./gradlew pixel2Api35DebugAndroidTest`
- **Build Release APK:** `./gradlew clean assembleRelease`
- **Build Release Bundle:** `./gradlew clean bundleRelease`
- **Lint/Check:** `./gradlew lint detekt kotlinterCheck`
- **Format Code:** `./gradlew formatKotlin`
- **Code Coverage:** `./gradlew :app:jacocoTestReportDebug`
- **Generate Baseline Profile:** `./gradlew generateBaselineProfile`

## Development Conventions
### Clean Architecture
The project strictly follows Clean Architecture principles with a focus on an **Offline-First** data flow:
- **`data`**: Implements repositories and data sources (local, network, preferences). Uses Room, Ktor, and DataStore. The repository acts as a coordinator, ensuring the local database remains the single source of truth.
- **`domain`**: Contains business logic, domain models, repository interfaces, and **Use Cases (Interactors)**. Pure Kotlin, no Android dependencies. Use Cases encapsulate specific business rules and provide a clean API for the UI layer.
- **`ui`**: Jetpack Compose implementation. Follows MVVM with `uiState` flows, `Effect` flows for one-time side effects, and `UIActions` for user-triggered events. ViewModels are reactive and update the UI automatically when domain data or user preferences change.
  - The Search screen uses **debounced automatic search**: `SearchViewModel` exposes a `keywordFlow` that applies `debounce(SEARCH_DEBOUNCE_MILLIS)` + `distinctUntilChanged` + `filter { isNotEmpty() }` to trigger searches as the user types. Calling `search()` (e.g., via the keyboard IME search action) fires immediately and resets the flow to cancel any pending debounce. Constants such as `SEARCH_DEBOUNCE_MILLIS` and `KEYWORD_MAX_LENGTH` live in `SearchViewModel.Companion`.

### Testing Strategy
- **Unit Tests:** Located in `src/test`. Focus on ViewModels and Domain logic.
- **Test Fixtures:** Manual fakes for repositories are provided in `src/testFixtures` to ensure reliable and fast unit tests without excessive mocking.
- **UI Tests:** Located in `src/androidTest`. Uses Compose UI testing and Hilt for dependency injection in tests.
- **Code Coverage:** Managed by JaCoCo.

### Coding Style
- Follows Kotlin coding standards enforced by **Kotlinter** and **Detekt**.
- Uses **Gradle Kotlin DSL** and **Version Catalog** (`libs.versions.toml`) for dependency management.
- Standardized file headers with copyright information.

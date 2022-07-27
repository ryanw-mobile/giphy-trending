package uk.ryanwong.giphytrending

/***
 * Workaround to rethrow CancellationException
 * Reference: https://github.com/Kotlin/kotlinx.coroutines/issues/1814
 */

inline fun <reified T : Throwable, R> Result<R>.except(): Result<R> =
    onFailure { if (it is T) throw it }
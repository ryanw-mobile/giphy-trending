/*
 * Copyright (c) 2024-2025. Ryan Wong
 * https://github.com/ryanw-mobile
 */

package com.rwmobi.giphytrending.domain.exceptions

sealed class NetworkException(message: String) : Throwable(message) {
    class NetworkTimeoutException(message: String = "Network request timed out") : NetworkException(message)
    class NetworkConnectionException(message: String = "No internet connection") : NetworkException(message)
    class NetworkServerException(message: String = "Server error occurred") : NetworkException(message)
    class NetworkUnknownException(message: String = "An unknown network error occurred") : NetworkException(message)
}

/*
 * Copyright (c) 2024. Ryan Wong
 * https://github.com/ryanw-mobile
 */

package com.rwmobi.giphytrending.domain

class EmptyGiphyAPIKeyException(message: String = "Giphy API key is empty") : Throwable(message) {}

/*
 * Copyright (c) 2024. Ryan Wong
 * https://github.com/ryanw-mobile
 */

package com.rwmobi.giphytrending.ui.utils

import android.content.Context
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

fun downloadImage(imageUrl: String, coroutineScope: CoroutineScope, context: Context, onError: suspend () -> Unit) {
    coroutineScope.launch {
        val result = withContext(Dispatchers.IO) {
            context.downloadImageUsingMediaStore(imageUrl)
        }
        if (!result) {
            withContext(Dispatchers.Main) {
                onError()
            }
        }
    }
}

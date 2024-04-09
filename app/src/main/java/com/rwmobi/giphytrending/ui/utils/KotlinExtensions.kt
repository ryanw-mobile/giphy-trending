package com.rwmobi.giphytrending.ui.utils

import android.content.Context
import android.content.Intent
import androidx.core.net.toUri

internal fun Context.startBrowserActivity(src: String) {
    val uri = src.toUri().buildUpon().scheme("https").build()
    val intent = Intent(Intent.ACTION_VIEW).setData(uri)
    startActivity(intent)
}

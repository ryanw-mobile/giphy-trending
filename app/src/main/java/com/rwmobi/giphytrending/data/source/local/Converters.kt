/*
 * Copyright (c) 2024. Ryan Wong
 * https://github.com/ryanw-mobile
 */

package com.rwmobi.giphytrending.data.source.local

import androidx.room.TypeConverter
import java.util.Date

class Converters {
    // Moshi has set to convert UTC strings to Date objects,
    // but Room does not know how to store them without these converters
    // (that convert them between Date and Long)
    @TypeConverter
    fun toDate(value: Long?): Date? {
        return value?.let { Date(it) }
    }

    @TypeConverter
    fun fromDate(date: Date?): Long? {
        return date?.time
    }
}

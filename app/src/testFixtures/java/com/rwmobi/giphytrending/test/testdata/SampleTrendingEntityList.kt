/*
 * Copyright (c) 2024. Ryan Wong
 * https://github.com/ryanw-mobile
 */

@file:OptIn(ExperimentalTime::class)

package com.rwmobi.giphytrending.test.testdata

import com.rwmobi.giphytrending.data.source.local.model.TrendingEntity
import kotlin.time.ExperimentalTime
import kotlin.time.Instant

object SampleTrendingEntityList {
    val singleEntityList = listOf(
        TrendingEntity(
            id = "uaIAIw3ELuk69mhZ5I",
            previewUrl = "https://media3.giphy.com/media/uaIAIw3ELuk69mhZ5I/200w.gif",
            previewWidth = 480,
            previewHeight = 480,
            imageUrl = "https://media3.giphy.com/media/uaIAIw3ELuk69mhZ5I/giphy.gif",
            webUrl = "https://giphy.com/gifs/creative-courage-vidhyan-as-a-nation-we-have-lot-of-obligations-no-obligation-uaIAIw3ELuk69mhZ5I",
            title = "Joe Biden GIF by Creative Courage",
            type = "gif",
            username = "creative-courage",
            trendingDateTime = Instant.fromEpochMilliseconds(-62170156725000),
            importDateTime = Instant.fromEpochMilliseconds(1605041010000),
        ),
    )

    val tripleEntityList = listOf(
        TrendingEntity(
            id = "5KF7hci72bv2ZbchuT",
            previewUrl = "https://media2.giphy.com/media/5KF7hci72bv2ZbchuT/200w.gif",
            previewWidth = 480,
            previewHeight = 360,
            imageUrl = "https://media2.giphy.com/media/5KF7hci72bv2ZbchuT/giphy.gif",
            webUrl = "https://giphy.com/gifs/studiosoriginals-cooking-chef-cook-5KF7hci72bv2ZbchuT",
            title = "Chef Cooking GIF by GIPHY Studios Originals",
            type = "gif",
            username = "studiosoriginals",
            trendingDateTime = Instant.fromEpochMilliseconds(1636679711000),
            importDateTime = Instant.fromEpochMilliseconds(1635978358000),
        ),
        TrendingEntity(
            id = "uaIAIw3ELuk69mhZ5I",
            previewUrl = "https://media3.giphy.com/media/uaIAIw3ELuk69mhZ5I/200w.gif",
            previewWidth = 480,
            previewHeight = 480,
            imageUrl = "https://media3.giphy.com/media/uaIAIw3ELuk69mhZ5I/giphy.gif",
            webUrl = "https://giphy.com/gifs/creative-courage-vidhyan-as-a-nation-we-have-lot-of-obligations-no-obligation-uaIAIw3ELuk69mhZ5I",
            title = "Joe Biden GIF by Creative Courage",
            type = "gif",
            username = "creative-courage",
            trendingDateTime = Instant.fromEpochMilliseconds(-62170156725000),
            importDateTime = Instant.fromEpochMilliseconds(1605041010000),
        ),
        TrendingEntity(
            id = "etKSrsbbKbqwW6vzOg",
            previewUrl = "https://media1.giphy.com/media/etKSrsbbKbqwW6vzOg/200w.gif",
            previewWidth = 480,
            previewHeight = 480,
            imageUrl = "https://media1.giphy.com/media/etKSrsbbKbqwW6vzOg/giphy.gif",
            webUrl = "https://giphy.com/gifs/studiosoriginals-chicken-dinner-winner-etKSrsbbKbqwW6vzOg",
            title = "Winner Winner Win GIF by GIPHY Studios Originals",
            type = "gif",
            username = "studiosoriginals",
            trendingDateTime = Instant.fromEpochMilliseconds(1636678809000),
            importDateTime = Instant.fromEpochMilliseconds(1636417528000),
        ),
    )
}

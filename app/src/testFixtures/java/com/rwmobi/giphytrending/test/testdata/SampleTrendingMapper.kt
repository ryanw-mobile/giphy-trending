/*
 * Copyright (c) 2024. Ryan Wong
 * https://github.com/ryanw-mobile
 */

package com.rwmobi.giphytrending.test.testdata

import com.rwmobi.giphytrending.data.source.local.TrendingEntity
import com.rwmobi.giphytrending.data.source.network.model.FixedHeight
import com.rwmobi.giphytrending.data.source.network.model.FixedWidth
import com.rwmobi.giphytrending.data.source.network.model.Images
import com.rwmobi.giphytrending.data.source.network.model.Original
import com.rwmobi.giphytrending.data.source.network.model.TrendingData
import com.rwmobi.giphytrending.domain.model.GiphyImageItem
import java.util.Date

internal object SampleTrendingMapper {

    val sampleTrendingData1 by lazy {
        TrendingData(
            analyticsResponsePayload = "some-analytics-response-payload",
            bitlyGifUrl = "https://some.url/some-path",
            bitlyUrl = "https://some.url/some-path",
            contentUrl = "",
            embedUrl = "https://some.url/some-path",
            id = "5KF7hci72bv2ZbchuT",
            images = Images(
                fixedHeight = FixedHeight(
                    height = "200",
                    mp4 = "https://some.url/some-path",
                    mp4Size = "101674",
                    size = "500824",
                    url = "https://some.url/some-path",
                    webp = "https://some.url/some-path",
                    webpSize = "227744",
                    width = "265",
                ),
                fixedWidth = FixedWidth(
                    height = "151",
                    mp4 = "https://some.url/some-path",
                    mp4Size = "65384",
                    size = "317527",
                    url = "https://media2.giphy.com/media/5KF7hci72bv2ZbchuT/200w.gif",
                    webp = "https://some.url/some-path",
                    webpSize = "153256",
                    width = "200",
                ),
                original = Original(
                    frames = "26",
                    hash = "some-hash",
                    height = "362",
                    mp4 = "https://some.url/some-path",
                    mp4Size = "273620",
                    size = "1749887",
                    url = "https://media2.giphy.com/media/5KF7hci72bv2ZbchuT/giphy.gif",
                    webp = "https://some.url/some-path",
                    webpSize = "455910",
                    width = "480",
                ),
            ),
            importDatetime = Date(1635978358000),
            isSticker = 0,
            rating = "g",
            slug = "moonagedaydream-neon-rated-moonage-daydream-UeaVRMdWRGzvzr62pF",
            source = "https://some.url/some-path",
            sourcePostUrl = "https://some.url/some-path",
            sourceTld = "neonrated.com",
            title = "Chef Cooking GIF by GIPHY Studios Originals",
            trendingDatetime = Date(1636679711000),
            type = "gif",
            url = "https://giphy.com/gifs/studiosoriginals-cooking-chef-cook-5KF7hci72bv2ZbchuT",
            username = "studiosoriginals",
        )
    }

    val sampleTrendingEntity1 by lazy {
        TrendingEntity(
            id = "5KF7hci72bv2ZbchuT",
            previewUrl = "https://media2.giphy.com/media/5KF7hci72bv2ZbchuT/200w.gif",
            imageUrl = "https://media2.giphy.com/media/5KF7hci72bv2ZbchuT/giphy.gif",
            webUrl = "https://giphy.com/gifs/studiosoriginals-cooking-chef-cook-5KF7hci72bv2ZbchuT",
            title = "Chef Cooking GIF by GIPHY Studios Originals",
            type = "gif",
            username = "studiosoriginals",
            trendingDateTime = Date(1636679711000),
            importDateTime = Date(1635978358000),
        )
    }

    val sampleDomainModel1 by lazy {
        GiphyImageItem(
            id = "5KF7hci72bv2ZbchuT",
            previewUrl = "https://media2.giphy.com/media/5KF7hci72bv2ZbchuT/200w.gif",
            imageUrl = "https://media2.giphy.com/media/5KF7hci72bv2ZbchuT/giphy.gif",
            webUrl = "https://giphy.com/gifs/studiosoriginals-cooking-chef-cook-5KF7hci72bv2ZbchuT",
            title = "Chef Cooking GIF by GIPHY Studios Originals",
            type = "gif",
            username = "studiosoriginals",
        )
    }

    val sampleTrendingData2 by lazy {
        TrendingData(
            analyticsResponsePayload = "some-analytics-response-payload",
            bitlyGifUrl = "https://some.url/some-path",
            bitlyUrl = "https://some.url/some-path",
            contentUrl = "",
            embedUrl = "https://some.url/some-path",
            id = "uaIAIw3ELuk69mhZ5I",
            images = Images(
                fixedHeight = FixedHeight(
                    height = "200",
                    mp4 = "https://some.url/some-path",
                    mp4Size = "101674",
                    size = "500824",
                    url = "https://some.url/some-path",
                    webp = "https://some.url/some-path",
                    webpSize = "227744",
                    width = "265",
                ),
                fixedWidth = FixedWidth(
                    height = "151",
                    mp4 = "https://some.url/some-path",
                    mp4Size = "65384",
                    size = "317527",
                    url = "https://media3.giphy.com/media/uaIAIw3ELuk69mhZ5I/200w.gif",
                    webp = "https://some.url/some-path",
                    webpSize = "153256",
                    width = "200",
                ),
                original = Original(
                    frames = "26",
                    hash = "some-hash",
                    height = "362",
                    mp4 = "https://some.url/some-path",
                    mp4Size = "273620",
                    size = "1749887",
                    url = "https://media3.giphy.com/media/uaIAIw3ELuk69mhZ5I/giphy.gif",
                    webp = "https://some.url/some-path",
                    webpSize = "455910",
                    width = "480",
                ),
            ),
            importDatetime = Date(1605041010000),
            isSticker = 0,
            rating = "g",
            slug = "moonagedaydream-neon-rated-moonage-daydream-UeaVRMdWRGzvzr62pF",
            source = "https://neonrated.com/films/bowie-moonage-daydream",
            sourcePostUrl = "https://neonrated.com/films/bowie-moonage-daydream",
            sourceTld = "neonrated.com",
            title = "Joe Biden GIF by Creative Courage",
            trendingDatetime = Date(-62170156725000),
            type = "gif",
            url = "https://giphy.com/gifs/creative-courage-vidhyan-as-a-nation-we-have-lot-of-obligations-no-obligation-uaIAIw3ELuk69mhZ5I",
            username = "creative-courage",
        )
    }

    val sampleTrendingEntity2 by lazy {
        TrendingEntity(
            id = "uaIAIw3ELuk69mhZ5I",
            previewUrl = "https://media3.giphy.com/media/uaIAIw3ELuk69mhZ5I/200w.gif",
            imageUrl = "https://media3.giphy.com/media/uaIAIw3ELuk69mhZ5I/giphy.gif",
            webUrl = "https://giphy.com/gifs/creative-courage-vidhyan-as-a-nation-we-have-lot-of-obligations-no-obligation-uaIAIw3ELuk69mhZ5I",
            title = "Joe Biden GIF by Creative Courage",
            type = "gif",
            username = "creative-courage",
            trendingDateTime = Date(-62170156725000),
            importDateTime = Date(1605041010000),
        )
    }

    val sampleDomainModel2 by lazy {
        GiphyImageItem(
            id = "uaIAIw3ELuk69mhZ5I",
            previewUrl = "https://media3.giphy.com/media/uaIAIw3ELuk69mhZ5I/200w.gif",
            imageUrl = "https://media3.giphy.com/media/uaIAIw3ELuk69mhZ5I/giphy.gif",
            webUrl = "https://giphy.com/gifs/creative-courage-vidhyan-as-a-nation-we-have-lot-of-obligations-no-obligation-uaIAIw3ELuk69mhZ5I",
            title = "Joe Biden GIF by Creative Courage",
            type = "gif",
            username = "creative-courage",
        )
    }

    val sampleTrendingData3 by lazy {
        TrendingData(
            analyticsResponsePayload = "some-analytics-response-payload",
            bitlyGifUrl = "https://some.url/some-path",
            bitlyUrl = "https://some.url/some-path",
            contentUrl = "",
            embedUrl = "https://some.url/some-path",
            id = "etKSrsbbKbqwW6vzOg",
            images = Images(
                fixedHeight = FixedHeight(
                    height = "200",
                    mp4 = "https://some.url/some-path",
                    mp4Size = "101674",
                    size = "500824",
                    url = "https://some.url/some-path",
                    webp = "https://some.url/some-path",
                    webpSize = "227744",
                    width = "265",
                ),
                fixedWidth = FixedWidth(
                    height = "151",
                    mp4 = "https://some.url/some-path",
                    mp4Size = "65384",
                    size = "317527",
                    url = "https://media1.giphy.com/media/etKSrsbbKbqwW6vzOg/200w.gif",
                    webp = "https://some.url/some-path",
                    webpSize = "153256",
                    width = "200",
                ),
                original = Original(
                    frames = "26",
                    hash = "some-hash",
                    height = "362",
                    mp4 = "https://some.url/some-path",
                    mp4Size = "273620",
                    size = "1749887",
                    url = "https://media1.giphy.com/media/etKSrsbbKbqwW6vzOg/giphy.gif",
                    webp = "https://some.url/some-path",
                    webpSize = "455910",
                    width = "480",
                ),
            ),
            importDatetime = Date(1636417528000),
            isSticker = 0,
            rating = "g",
            slug = "moonagedaydream-neon-rated-moonage-daydream-UeaVRMdWRGzvzr62pF",
            source = "https://neonrated.com/films/bowie-moonage-daydream",
            sourcePostUrl = "https://neonrated.com/films/bowie-moonage-daydream",
            sourceTld = "neonrated.com",
            title = "Winner Winner Win GIF by GIPHY Studios Originals",
            trendingDatetime = Date(1636678809000),
            type = "gif",
            url = "https://giphy.com/gifs/studiosoriginals-chicken-dinner-winner-etKSrsbbKbqwW6vzOg",
            username = "studiosoriginals",
        )
    }

    val sampleTrendingEntity3 by lazy {
        TrendingEntity(
            "etKSrsbbKbqwW6vzOg",
            previewUrl = "https://media1.giphy.com/media/etKSrsbbKbqwW6vzOg/200w.gif",
            imageUrl = "https://media1.giphy.com/media/etKSrsbbKbqwW6vzOg/giphy.gif",
            webUrl = "https://giphy.com/gifs/studiosoriginals-chicken-dinner-winner-etKSrsbbKbqwW6vzOg",
            title = "Winner Winner Win GIF by GIPHY Studios Originals",
            type = "gif",
            username = "studiosoriginals",
            trendingDateTime = Date(1636678809000),
            importDateTime = Date(1636417528000),
        )
    }

    val sampleDomainModel3 by lazy {
        GiphyImageItem(
            "etKSrsbbKbqwW6vzOg",
            previewUrl = "https://media1.giphy.com/media/etKSrsbbKbqwW6vzOg/200w.gif",
            imageUrl = "https://media1.giphy.com/media/etKSrsbbKbqwW6vzOg/giphy.gif",
            webUrl = "https://giphy.com/gifs/studiosoriginals-chicken-dinner-winner-etKSrsbbKbqwW6vzOg",
            title = "Winner Winner Win GIF by GIPHY Studios Originals",
            type = "gif",
            username = "studiosoriginals",
        )
    }
}
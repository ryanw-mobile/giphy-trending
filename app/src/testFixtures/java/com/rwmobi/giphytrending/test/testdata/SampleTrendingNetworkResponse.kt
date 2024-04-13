/*
 * Copyright (c) 2024. Ryan Wong
 * https://github.com/ryanw-mobile
 */

package com.rwmobi.giphytrending.test.testdata

import com.rwmobi.giphytrending.data.source.network.model.FixedHeight
import com.rwmobi.giphytrending.data.source.network.model.FixedWidth
import com.rwmobi.giphytrending.data.source.network.model.Images
import com.rwmobi.giphytrending.data.source.network.model.Meta
import com.rwmobi.giphytrending.data.source.network.model.Original
import com.rwmobi.giphytrending.data.source.network.model.Pagination
import com.rwmobi.giphytrending.data.source.network.model.TrendingData
import com.rwmobi.giphytrending.data.source.network.model.TrendingNetworkResponse
import java.util.Date

object SampleTrendingNetworkResponse {
    val jobBidenResponse = TrendingNetworkResponse(
        meta = Meta(
            msg = "some-msg",
            responseId = "some-response-id",
            status = 0,
        ),
        trendingData = listOf(
            TrendingData(
                analyticsResponsePayload = "",
                bitlyGifUrl = "",
                bitlyUrl = "",
                contentUrl = "",
                embedUrl = "",
                id = "uaIAIw3ELuk69mhZ5I",
                images = Images(
                    fixedHeight = FixedHeight(
                        height = "",
                        mp4 = null,
                        mp4Size = null,
                        size = "",
                        url = "",
                        webp = null,
                        webpSize = null,
                        width = "",
                    ),
                    fixedWidth = FixedWidth(
                        height = "",
                        mp4 = null,
                        mp4Size = null,
                        size = "",
                        url = "https://media3.giphy.com/media/uaIAIw3ELuk69mhZ5I/200w.gif",
                        webp = null,
                        webpSize = null,
                        width = "",
                    ),
                    original = Original(
                        frames = "",
                        hash = "",
                        height = "",
                        mp4 = null,
                        mp4Size = null,
                        size = "",
                        url = "https://media3.giphy.com/media/uaIAIw3ELuk69mhZ5I/giphy.gif",
                        webp = null,
                        webpSize = null,
                        width = "",
                    ),
                ),
                importDatetime = Date(1605041010000),
                isSticker = 0,
                rating = "",
                slug = "",
                source = "",
                sourcePostUrl = "",
                sourceTld = "",
                title = "Joe Biden GIF by Creative Courage",
                trendingDatetime = Date(-62170156725000),
                type = "gif",
                url = "https://giphy.com/gifs/creative-courage-vidhyan-as-a-nation-we-have-lot-of-obligations-no-obligation-uaIAIw3ELuk69mhZ5I",
                username = "creative-courage",
            ),
        ),
        pagination = Pagination(
            count = 0,
            offset = 0,
            totalCount = 0,
        ),
    )
}

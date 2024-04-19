/*
 * Copyright (c) 2024. Ryan Wong
 * https://github.com/ryanw-mobile
 */

package com.rwmobi.giphytrending.test.testdata

import com.rwmobi.giphytrending.data.source.network.dto.FixedHeight
import com.rwmobi.giphytrending.data.source.network.dto.FixedWidth
import com.rwmobi.giphytrending.data.source.network.dto.Images
import com.rwmobi.giphytrending.data.source.network.dto.MetaDto
import com.rwmobi.giphytrending.data.source.network.dto.Original
import com.rwmobi.giphytrending.data.source.network.dto.PaginationDto
import com.rwmobi.giphytrending.data.source.network.dto.SearchNetworkResponseDto
import com.rwmobi.giphytrending.data.source.network.dto.TrendingDataDto
import java.util.Date

object SampleSearchNetworkResponse {
    val singleResponse = SearchNetworkResponseDto(
        metaDto = MetaDto(
            msg = "OK",
            responseId = "g52knjbgx1u2x935oeod0s77dq4wd8dozlcs1xgm",
            status = 200,
        ),
        trendingData = listOf(
            TrendingDataDto(
                analyticsResponsePayload = "",
                bitlyGifUrl = "",
                bitlyUrl = "",
                contentUrl = "",
                embedUrl = "",
                id = "uaIAIw3ELuk69mhZ5I",
                images = Images(
                    fixedHeight = FixedHeight(
                        height = "480",
                        mp4 = null,
                        mp4Size = null,
                        size = "",
                        url = "",
                        webp = null,
                        webpSize = null,
                        width = "480",
                    ),
                    fixedWidth = FixedWidth(
                        height = "480",
                        mp4 = null,
                        mp4Size = null,
                        size = "",
                        url = "https://media3.giphy.com/media/uaIAIw3ELuk69mhZ5I/200w.gif",
                        webp = null,
                        webpSize = null,
                        width = "480",
                    ),
                    original = Original(
                        frames = "",
                        hash = "",
                        height = "480",
                        mp4 = null,
                        mp4Size = null,
                        size = "",
                        url = "https://media3.giphy.com/media/uaIAIw3ELuk69mhZ5I/giphy.gif",
                        webp = null,
                        webpSize = null,
                        width = "480",
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
        pagination = PaginationDto(
            count = 1,
            offset = 0,
            totalCount = 1,
        ),
    )
}

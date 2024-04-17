/*
 * Copyright (c) 2024. Ryan Wong
 * https://github.com/ryanw-mobile
 */

package com.rwmobi.giphytrending.data.source.network

import com.rwmobi.giphytrending.data.source.network.dto.FixedHeight
import com.rwmobi.giphytrending.data.source.network.dto.FixedWidth
import com.rwmobi.giphytrending.data.source.network.dto.Images
import com.rwmobi.giphytrending.data.source.network.dto.MetaDto
import com.rwmobi.giphytrending.data.source.network.dto.Original
import com.rwmobi.giphytrending.data.source.network.dto.PaginationDto
import com.rwmobi.giphytrending.data.source.network.dto.TrendingDataDto
import com.rwmobi.giphytrending.data.source.network.dto.TrendingNetworkResponseDto
import java.util.Date

object SampleTrendingNetworkResponseDto {
    val trendingNetworkResponseDto = TrendingNetworkResponseDto(
        metaDto = MetaDto(
            msg = "OK",
            responseId = "g52knjbgx1u2x935oeod0s77dq4wd8dozlcs1xgm",
            status = 200,
        ),
        pagination = PaginationDto(
            count = 50,
            offset = 2,
            totalCount = 2809,
        ),
        trendingData = listOf(
            TrendingDataDto(
                analyticsResponsePayload = "e=ZXZlbnRfdHlwZT1HSUZfVFJFTkRJTkcmY2lkPTk4MzYyNj...",
                bitlyGifUrl = "https://gph.is/g/Z2Rg9Gb",
                bitlyUrl = "https://gph.is/g/Z2Rg9Gb",
                contentUrl = "",
                embedUrl = "https://giphy.com/embed/GgmJB2XL0Kfx8qrA8q",
                id = "GgmJB2XL0Kfx8qrA8q",
                images = Images(
                    fixedHeight = FixedHeight(
                        height = "200",
                        mp4 = "https://media0.giphy.com/media/GgmJB2XL0Kfx8qrA8q/200.mp4?cid=98362685g52knjbgx1u2x935oeod0s77dq4wd8dozlcs1xgm&ep=v1_gifs_trending&rid=200.mp4&ct=g",
                        mp4Size = "154676",
                        size = "866562",
                        url = "https://media0.giphy.com/media/GgmJB2XL0Kfx8qrA8q/200.gif?cid=98362685g52knjbgx1u2x935oeod0s77dq4wd8dozlcs1xgm&ep=v1_gifs_trending&rid=200.gif&ct=g",
                        webp = "https://media0.giphy.com/media/GgmJB2XL0Kfx8qrA8q/200.webp?cid=98362685g52knjbgx1u2x935oeod0s77dq4wd8dozlcs1xgm&ep=v1_gifs_trending&rid=200.webp&ct=g",
                        webpSize = "254750",
                        width = "200",
                    ),
                    fixedWidth = FixedWidth(
                        height = "200",
                        mp4 = "https://media0.giphy.com/media/GgmJB2XL0Kfx8qrA8q/200w.mp4?cid=98362685g52knjbgx1u2x935oeod0s77dq4wd8dozlcs1xgm&ep=v1_gifs_trending&rid=200w.mp4&ct=g",
                        mp4Size = "154676",
                        size = "866562",
                        url = "https://media0.giphy.com/media/GgmJB2XL0Kfx8qrA8q/200w.gif?cid=98362685g52knjbgx1u2x935oeod0s77dq4wd8dozlcs1xgm&ep=v1_gifs_trending&rid=200w.gif&ct=g",
                        webp = "https://media0.giphy.com/media/GgmJB2XL0Kfx8qrA8q/200w.webp?cid=98362685g52knjbgx1u2x935oeod0s77dq4wd8dozlcs1xgm&ep=v1_gifs_trending&rid=200w.webp&ct=g",
                        webpSize = "254750",
                        width = "200",
                    ),
                    original = Original(
                        frames = "57",
                        hash = "0a87be1df7f514f5b9575dc767871595",
                        height = "450",
                        mp4 = "https://media0.giphy.com/media/GgmJB2XL0Kfx8qrA8q/giphy.mp4?cid=98362685g52knjbgx1u2x935oeod0s77dq4wd8dozlcs1xgm&ep=v1_gifs_trending&rid=giphy.mp4&ct=g",
                        mp4Size = "574513",
                        size = "3867887",
                        url = "https://media0.giphy.com/media/GgmJB2XL0Kfx8qrA8q/giphy.gif?cid=98362685g52knjbgx1u2x935oeod0s77dq4wd8dozlcs1xgm&ep=v1_gifs_trending&rid=giphy.gif&ct=g",
                        webp = "https://media0.giphy.com/media/GgmJB2XL0Kfx8qrA8q/giphy.webp?cid=98362685g52knjbgx1u2x935oeod0s77dq4wd8dozlcs1xgm&ep=v1_gifs_trending&rid=giphy.webp&ct=g",
                        webpSize = "812042",
                        width = "450",
                    ),
                ),
                importDatetime = Date(1713374040000),
                isSticker = 0,
                rating = "g",
                slug = "watfordfootballclub-reaction-celebration-kiss-GgmJB2XL0Kfx8qrA8q",
                source = "",
                sourcePostUrl = "",
                sourceTld = "",
                title = "I Love You Kiss GIF by Watford Football Club",
                trendingDatetime = Date(1711963245000),
                type = "gif",
                url = "https://giphy.com/gifs/watfordfootballclub-reaction-celebration-kiss-GgmJB2XL0Kfx8qrA8q",
                username = "watfordfootballclub",
            ),
            // Add more TrendingDataDto instances here as needed
        ),
    )
}

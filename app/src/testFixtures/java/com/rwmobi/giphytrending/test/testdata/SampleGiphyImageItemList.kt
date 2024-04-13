/*
 * Copyright (c) 2024. Ryan Wong
 * https://github.com/ryanw-mobile
 */

package com.rwmobi.giphytrending.test.testdata

import com.rwmobi.giphytrending.domain.model.GiphyImageItem

object SampleGiphyImageItemList {
    val giphyImageItemList = listOf(
        GiphyImageItem(
            id = "some-id-1",
            previewUrl = "some-preview-url-1",
            imageUrl = "some-image-url-1",
            webUrl = "some-web-url-1",
            title = "some-title-1",
            type = "some-type-1",
            username = "some-user-name-1",
        ),
        GiphyImageItem(
            id = "some-id-2",
            previewUrl = "some-preview-url-2",
            imageUrl = "some-image-url-2",
            webUrl = "some-web-url-2",
            title = "some-title-2",
            type = "some-type-2",
            username = "some-user-name-2",
        ),
    )
}

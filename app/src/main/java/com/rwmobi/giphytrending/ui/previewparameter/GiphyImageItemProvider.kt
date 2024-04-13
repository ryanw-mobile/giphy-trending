/*
 * Copyright (c) 2024. Ryan Wong
 * https://github.com/ryanw-mobile
 */

package com.rwmobi.giphytrending.ui.previewparameter

import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import com.rwmobi.giphytrending.domain.model.GiphyImageItem

class GiphyImageItemProvider : PreviewParameterProvider<GiphyImageItem> {
    override val values: Sequence<GiphyImageItem>
        get() = sequenceOf(
            GiphyImageItem(
                id = "MiBE4zcYUZWYmHnsI7",
                previewUrl = "https://media4.giphy.com/media/MiBE4zcYUZWYmHnsI7/200w.gif",
                imageUrl = "https://media4.giphy.com/media/MiBE4zcYUZWYmHnsI7/giphy.gif",
                webUrl = "https://giphy.com/gifs/marchmadness-sports-sport-womens-basketball-MiBE4zcYUZWYmHnsI7",
                title = "Womens Basketball Sport GIF by NCAA March Madness",
                type = "gif",
                username = "marchmadness",
            ),
        )
}

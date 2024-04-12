/*
 * Copyright (c) 2024. Ryan Wong
 * https://github.com/ryanw-mobile
 */

package com.rwmobi.giphytrending.domain.repository

import com.rwmobi.giphytrending.domain.model.GiphyImageItem
import com.rwmobi.giphytrending.domain.model.Rating

interface GiphyRepository {

    suspend fun fetchCachedTrending(): Result<List<GiphyImageItem>>
    suspend fun reloadTrending(limit: Int, rating: Rating): Result<List<GiphyImageItem>>
}

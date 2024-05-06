/*
 * Copyright (c) 2024. Ryan Wong
 * https://github.com/ryanw-mobile
 */

package com.rwmobi.giphytrending.domain.repository

import com.rwmobi.giphytrending.domain.model.GifObject
import com.rwmobi.giphytrending.domain.model.Rating

interface TrendingRepository {

    suspend fun fetchCachedTrending(): Result<List<GifObject>>
    suspend fun reloadTrending(limit: Int, rating: Rating): Result<List<GifObject>>
}

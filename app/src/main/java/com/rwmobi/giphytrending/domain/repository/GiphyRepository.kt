/*
 * Copyright (c) 2024. Ryan Wong
 * https://github.com/ryanw-mobile
 */

package com.rwmobi.giphytrending.domain.repository

import com.rwmobi.giphytrending.domain.model.GiphyImageItem

interface GiphyRepository {

    suspend fun fetchCachedTrending(): Result<List<GiphyImageItem>>
    suspend fun reloadTrending(apiMaxEntries: Int): Result<List<GiphyImageItem>>
}

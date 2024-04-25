/*
 * Copyright (c) 2024. Ryan Wong
 * https://github.com/ryanw-mobile
 */

package com.rwmobi.giphytrending.domain.repository

import com.rwmobi.giphytrending.domain.model.GiphyImageItem
import com.rwmobi.giphytrending.domain.model.Rating

interface SearchRepository {

    suspend fun search(keyword: String?, limit: Int, rating: Rating): Result<List<GiphyImageItem>>
    fun getLastSuccessfulSearchKeyword(): String?
    fun getLastSuccessfulSearchResults(): List<GiphyImageItem>?
}

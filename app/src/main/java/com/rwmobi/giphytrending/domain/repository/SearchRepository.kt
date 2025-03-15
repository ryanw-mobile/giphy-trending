/*
 * Copyright (c) 2024-2025. Ryan Wong
 * https://github.com/ryanw-mobile
 */

package com.rwmobi.giphytrending.domain.repository

import com.rwmobi.giphytrending.domain.model.GifObject
import com.rwmobi.giphytrending.domain.model.Rating

interface SearchRepository {

    suspend fun search(keyword: String?, limit: Int, rating: Rating): Result<List<GifObject>>
    fun getLastSuccessfulSearchKeyword(): String?
    fun getLastSuccessfulSearchResults(): List<GifObject>?
}

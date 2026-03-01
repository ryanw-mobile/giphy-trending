/*
 * Copyright (c) 2024-2026. Ryan Wong
 * https://github.com/ryanw-mobile
 */

package com.rwmobi.giphytrending.domain.repository

import com.rwmobi.giphytrending.domain.model.GifObject
import com.rwmobi.giphytrending.domain.model.Rating

/**
 * Repository interface for searching GIFs.
 */
interface SearchRepository {

    /**
     * Searches for GIFs based on a keyword.
     *
     * @param keyword The keyword to search for.
     * @param limit The maximum number of results to return.
     * @param rating The content rating filter for the GIFs.
     * @return A [Result] containing the list of [GifObject] search results.
     */
    suspend fun search(keyword: String?, limit: Int, rating: Rating): Result<List<GifObject>>

    /**
     * Returns the keyword used in the last successful search.
     */
    fun getLastSuccessfulSearchKeyword(): String?

    /**
     * Returns the results of the last successful search.
     */
    fun getLastSuccessfulSearchResults(): List<GifObject>?
}

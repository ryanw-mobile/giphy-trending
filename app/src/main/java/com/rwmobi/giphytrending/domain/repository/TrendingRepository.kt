/*
 * Copyright (c) 2024-2025. Ryan Wong
 * https://github.com/ryanw-mobile
 */

package com.rwmobi.giphytrending.domain.repository

import com.rwmobi.giphytrending.domain.model.GifObject
import com.rwmobi.giphytrending.domain.model.Rating
import kotlinx.coroutines.flow.Flow

/**
 * Repository interface for managing trending GIF data.
 */
interface TrendingRepository {

    /**
     * A [Flow] of the trending GIFs, observed from the local database.
     */
    val trendingFlow: Flow<List<GifObject>>

    /**
     * Triggers a refresh of the trending GIFs from the network and updates the local database.
     *
     * @param limit The maximum number of GIFs to fetch.
     * @param rating The content rating filter for the GIFs.
     * @return A [Result] indicating success or failure of the refresh operation.
     */
    suspend fun refreshTrending(limit: Int, rating: Rating): Result<Unit>
}

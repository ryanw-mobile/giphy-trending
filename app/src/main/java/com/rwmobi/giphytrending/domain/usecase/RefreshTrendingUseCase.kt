/*
 * Copyright (c) 2024-2026. Ryan Wong
 * https://github.com/ryanw-mobile
 */

package com.rwmobi.giphytrending.domain.usecase

import com.rwmobi.giphytrending.domain.model.Rating
import com.rwmobi.giphytrending.domain.repository.TrendingRepository
import javax.inject.Inject

/**
 * Use case to trigger a refresh of the trending GIFs from the network.
 */
class RefreshTrendingUseCase @Inject constructor(
    private val trendingRepository: TrendingRepository,
) {
    suspend operator fun invoke(limit: Int, rating: Rating): Result<Unit> = trendingRepository.refreshTrending(limit = limit, rating = rating)
}

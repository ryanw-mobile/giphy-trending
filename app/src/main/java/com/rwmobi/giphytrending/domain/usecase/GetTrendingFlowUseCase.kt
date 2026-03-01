/*
 * Copyright (c) 2024-2026. Ryan Wong
 * https://github.com/ryanw-mobile
 */

package com.rwmobi.giphytrending.domain.usecase

import com.rwmobi.giphytrending.domain.model.GifObject
import com.rwmobi.giphytrending.domain.repository.TrendingRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/**
 * Use case to get the trending GIFs as a Flow.
 */
class GetTrendingFlowUseCase @Inject constructor(
    private val trendingRepository: TrendingRepository,
) {
    operator fun invoke(): Flow<List<GifObject>> = trendingRepository.trendingFlow
}

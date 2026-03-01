/*
 * Copyright (c) 2024-2026. Ryan Wong
 * https://github.com/ryanw-mobile
 */

package com.rwmobi.giphytrending.domain.usecase

import com.rwmobi.giphytrending.domain.model.GifObject
import com.rwmobi.giphytrending.domain.model.Rating
import com.rwmobi.giphytrending.domain.repository.SearchRepository
import javax.inject.Inject

/**
 * Use case to search for GIFs based on a keyword.
 */
class SearchGifsUseCase @Inject constructor(
    private val searchRepository: SearchRepository,
) {
    suspend operator fun invoke(keyword: String?, limit: Int, rating: Rating): Result<List<GifObject>> = searchRepository.search(keyword = keyword, limit = limit, rating = rating)
}

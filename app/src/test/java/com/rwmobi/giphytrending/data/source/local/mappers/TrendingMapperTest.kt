/*
 * Copyright (c) 2024-2025. Ryan Wong
 * https://github.com/ryanw-mobile
 */

package com.rwmobi.giphytrending.data.source.local.mappers

import com.rwmobi.giphytrending.data.repository.mappers.toEntity
import com.rwmobi.giphytrending.data.repository.mappers.toGifObject
import com.rwmobi.giphytrending.test.testdata.SampleTrendingMapper
import org.junit.Test
import kotlin.test.assertEquals

internal class TrendingMapperTest {
    /***
     * These tests are to make sure we preserve the exact object conversion.
     * When tests found broken, it suggested that we might have to check if the changes are intended.
     */

    // Test function names reviewed by Gemini for consistency

    @Test
    fun `returns correct GiphyImageItem when converting from TrendingEntity`() {
        val giphyImageItemDomainModel = SampleTrendingMapper.sampleTrendingEntity1.toGifObject()
        assertEquals(SampleTrendingMapper.sampleDomainModel1, giphyImageItemDomainModel)
    }

    @Test
    fun `returns list of GiphyImageItems when converting list of TrendingEntities`() {
        val trendingEntityList = listOf(
            SampleTrendingMapper.sampleTrendingEntity1,
            SampleTrendingMapper.sampleTrendingEntity2,
            SampleTrendingMapper.sampleTrendingEntity3,
        )
        val expectedGiphyImageItemDomainModelList = listOf(
            SampleTrendingMapper.sampleDomainModel1,
            SampleTrendingMapper.sampleDomainModel2,
            SampleTrendingMapper.sampleDomainModel3,
        )

        val giphyImageItemDomainModelList = trendingEntityList.map { it.toGifObject() }

        assertEquals(expectedGiphyImageItemDomainModelList, giphyImageItemDomainModelList)
    }

    @Test
    fun `returns correct TrendingEntity when converting from TrendingData`() {
        val trendingEntity = SampleTrendingMapper.sampleTrendingDataDto1.toEntity()
        assertEquals(SampleTrendingMapper.sampleTrendingEntity1, trendingEntity)
    }

    @Test
    fun `returns list of TrendingEntities when converting list of TrendingData`() {
        val mockTrendingDataList = listOf(
            SampleTrendingMapper.sampleTrendingDataDto1,
            SampleTrendingMapper.sampleTrendingDataDto2,
            SampleTrendingMapper.sampleTrendingDataDto3,
        )
        val expectedTrendingEntityList = listOf(
            SampleTrendingMapper.sampleTrendingEntity1,
            SampleTrendingMapper.sampleTrendingEntity2,
            SampleTrendingMapper.sampleTrendingEntity3,
        )

        val trendingEntityList = mockTrendingDataList.map { it.toEntity() }

        assertEquals(expectedTrendingEntityList, trendingEntityList)
    }
}

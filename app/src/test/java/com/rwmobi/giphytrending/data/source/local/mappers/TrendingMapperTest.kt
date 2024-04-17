/*
 * Copyright (c) 2024. Ryan Wong
 * https://github.com/ryanw-mobile
 */

package com.rwmobi.giphytrending.data.source.local.mappers

import com.rwmobi.giphytrending.test.testdata.SampleTrendingMapper
import io.kotest.matchers.collections.shouldContainExactly
import io.kotest.matchers.shouldBe
import org.junit.Test

internal class TrendingMapperTest {
    /***
     * These tests are to make sure we preserve the exact object conversion.
     * When tests found broken, it suggested that we might have to check if the changes are intended.
     */

    @Test
    fun `toDomainModel should correctly convert from TrendingEntity to GiphyImageItemDomainModel`() {
        val giphyImageItemDomainModel = SampleTrendingMapper.sampleTrendingEntity1.asGiphyImageItem()
        giphyImageItemDomainModel shouldBe SampleTrendingMapper.sampleDomainModel1
    }

    @Test
    fun `toDomainModelList should correctly convert from list of TrendingEntity to list of GiphyImageItemDomainModel`() {
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

        val giphyImageItemDomainModelList = trendingEntityList.asGiphyImageItem()

        giphyImageItemDomainModelList shouldContainExactly expectedGiphyImageItemDomainModelList
    }

    @Test
    fun `toTrendingEntity should correctly convert from TrendingData to TrendingEntity`() {
        val trendingEntity = SampleTrendingMapper.sampleTrendingDataDto1.asTrendingEntity()
        trendingEntity shouldBe SampleTrendingMapper.sampleTrendingEntity1
    }

    @Test
    fun `toTrendingEntityList should correctly convert from TrendingData to list of TrendingEntity`() {
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

        val trendingEntityList = mockTrendingDataList.asTrendingEntity()

        trendingEntityList shouldBe expectedTrendingEntityList
    }
}

package com.rwmobi.giphytrending.data.source.local

import com.rwmobi.giphytrending.test.testdata.SampleTrendingMapper
import io.kotest.core.spec.style.FreeSpec
import io.kotest.matchers.collections.shouldContainExactly
import io.kotest.matchers.shouldBe

internal class TrendingMapperTest : FreeSpec() {
    /***
     * These tests are to make sure we preserve the exact object conversion.
     * When tests found broken, it suggested that we might have to check if the changes are intended.
     */

    init {
        "toDomainModel" - {
            "should correctly convert from TrendingEntity to GiphyImageItemDomainModel" {
                val giphyImageItemDomainModel = SampleTrendingMapper.sampleTrendingEntity1.toDomainModel()

                giphyImageItemDomainModel shouldBe SampleTrendingMapper.sampleDomainModel1
            }
        }

        "toDomainModelList" - {
            "should correctly convert from list of TrendingEntity to list of GiphyImageItemDomainModel" {
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

                val giphyImageItemDomainModelList = trendingEntityList.toDomainModelList()

                giphyImageItemDomainModelList shouldContainExactly expectedGiphyImageItemDomainModelList
            }
        }

        "toTrendingEntity" - {
            "should correctly convert from TrendingData to TrendingEntity" {
                val trendingEntity = SampleTrendingMapper.sampleTrendingData1.toTrendingEntity()

                trendingEntity shouldBe SampleTrendingMapper.sampleTrendingEntity1
            }
        }

        "toTrendingEntityList" - {
            "should correctly convert from TrendingData to list of TrendingEntity" {
                val mockTrendingDataList = listOf(
                    SampleTrendingMapper.sampleTrendingData1,
                    SampleTrendingMapper.sampleTrendingData2,
                    SampleTrendingMapper.sampleTrendingData3,
                )
                val expectedTrendingEntityList = listOf(
                    SampleTrendingMapper.sampleTrendingEntity1,
                    SampleTrendingMapper.sampleTrendingEntity2,
                    SampleTrendingMapper.sampleTrendingEntity3,
                )

                val trendingEntityList = mockTrendingDataList.toTrendingEntityList()

                trendingEntityList shouldBe expectedTrendingEntityList
            }
        }
    }
}

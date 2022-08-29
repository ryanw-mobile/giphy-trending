package uk.ryanwong.giphytrending.data.source.local

import io.kotest.core.spec.style.FreeSpec
import io.kotest.matchers.collections.shouldContainExactly
import io.kotest.matchers.shouldBe

internal class TrendingMapperKtTest : FreeSpec() {

    /***
     * These tests are to make sure we preserve the exact object conversion.
     * When tests found broken, it suggested that we might have to check if the changes are intended.
     */

    // To Greg: I believe using real sample data here makes sense because we test real object conversion.
    // Feel free to challenge me when we have a chance working together again.

    init {
        "toDomainModel" - {
            "should correctly convert from TrendingEntity to GiphyImageItemDomainModel" {
                // 🔴 Given - mockTrendingEntity1

                // 🟡 When
                val giphyImageItemDomainModel =
                    TrendingMapperKtTestData.mockTrendingEntity1.toDomainModel()

                // 🟢 Then
                giphyImageItemDomainModel shouldBe TrendingMapperKtTestData.mockDomainModel1
            }
        }

        "toDomainModelList" - {
            "should correctly convert from list of TrendingEntity to list of GiphyImageItemDomainModel" {
                // 🔴 Given
                val trendingEntityList = listOf(
                    TrendingMapperKtTestData.mockTrendingEntity1,
                    TrendingMapperKtTestData.mockTrendingEntity2,
                    TrendingMapperKtTestData.mockTrendingEntity3
                )
                val expectedGiphyImageItemDomainModelList = listOf(
                    TrendingMapperKtTestData.mockDomainModel1,
                    TrendingMapperKtTestData.mockDomainModel2,
                    TrendingMapperKtTestData.mockDomainModel3
                )

                // 🟡 When
                val giphyImageItemDomainModelList = trendingEntityList.toDomainModelList()

                // 🟢 Then
                giphyImageItemDomainModelList shouldContainExactly expectedGiphyImageItemDomainModelList
            }
        }

        "toTrendingEntity" - {
            "should correctly convert from TrendingData to TrendingEntity" {
                // 🔴 Given - mockTrendingData1

                // 🟡 When
                val trendingEntity =
                    TrendingMapperKtTestData.mockTrendingData1.toTrendingEntity()

                // 🟢 Then
                trendingEntity shouldBe TrendingMapperKtTestData.mockTrendingEntity1
            }
        }

        "toTrendingEntityList" - {
            "should correctly convert from TrendingData to list of TrendingEntity" {
                // 🔴 Given -
                val mockTrendingDataList = listOf(
                    TrendingMapperKtTestData.mockTrendingData1,
                    TrendingMapperKtTestData.mockTrendingData2,
                    TrendingMapperKtTestData.mockTrendingData3
                )
                val expectedTrendingEntityList = listOf(
                    TrendingMapperKtTestData.mockTrendingEntity1,
                    TrendingMapperKtTestData.mockTrendingEntity2,
                    TrendingMapperKtTestData.mockTrendingEntity3
                )

                // 🟡 When
                val trendingEntityList =
                    mockTrendingDataList.toTrendingEntityList()

                // 🟢 Then
                trendingEntityList shouldBe expectedTrendingEntityList
            }
        }
    }
}

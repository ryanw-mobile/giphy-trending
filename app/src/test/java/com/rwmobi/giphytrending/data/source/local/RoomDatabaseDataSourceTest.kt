/*
 * Copyright (c) 2024. Ryan Wong
 * https://github.com/ryanw-mobile
 */

package com.rwmobi.giphytrending.data.source.local

import android.content.Context
import android.os.Build
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.rwmobi.giphytrending.test.testdata.SampleTrendingEntity
import com.rwmobi.giphytrending.test.testdata.SampleTrendingEntityList
import io.kotest.matchers.collections.shouldContainOnly
import io.kotest.matchers.shouldBe
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [Build.VERSION_CODES.P])
class RoomDatabaseDataSourceTest {
    private lateinit var giphyDatabase: GiphyDatabase
    private lateinit var roomDatabaseDataSource: RoomDatabaseDataSource

    @Before
    fun setup() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        giphyDatabase = Room.inMemoryDatabaseBuilder(context, GiphyDatabase::class.java)
            .allowMainThreadQueries()
            .build()

        roomDatabaseDataSource = RoomDatabaseDataSource(giphyDatabase = giphyDatabase)
    }

    @After
    fun teardown() {
        giphyDatabase.close()
    }

    // Test function names reviewed by ChatGPT for consistency

    @Test
    fun insertData_WithSingleEntity_ShouldStoreAndRetrieveEntityCorrectly() = runTest {
        val testData = SampleTrendingEntity.case1
        roomDatabaseDataSource.insertData(data = testData)

        val result = roomDatabaseDataSource.queryData()

        result.size shouldBe 1
        result[0] shouldBe testData
    }

    @Test
    fun insertAllData_WithMultipleEntities_ShouldStoreAndRetrieveAllEntitiesCorrectly() = runTest {
        val testDataList = SampleTrendingEntityList.tripleEntityList
        roomDatabaseDataSource.insertAllData(data = testDataList)

        val result = roomDatabaseDataSource.queryData()

        result.size shouldBe testDataList.size
        result shouldContainOnly testDataList
    }

    @Test
    fun clearData_AfterInserting_ShouldEmptyDatabaseSuccessfully() = runTest {
        val testDataList = SampleTrendingEntityList.tripleEntityList
        roomDatabaseDataSource.insertAllData(data = testDataList)

        roomDatabaseDataSource.clear()
        val result = roomDatabaseDataSource.queryData()

        result.size shouldBe 0
    }

    @Test
    fun markDirty_AfterInserting_ShouldSetDirtyFlagForAllEntries() = runTest {
        val testDataList = SampleTrendingEntityList.tripleEntityList
        roomDatabaseDataSource.insertAllData(data = testDataList)

        roomDatabaseDataSource.markDirty()
        val result = roomDatabaseDataSource.queryData()

        result.forEach { trendingEntity ->
            trendingEntity.dirty shouldBe true
        }
    }

    @Test
    fun deleteDirty_WhenDataIsMarkedDirty_ShouldRemoveAllDirtyEntries() = runTest {
        val testDataList = SampleTrendingEntityList.tripleEntityList
        roomDatabaseDataSource.insertAllData(data = testDataList)
        roomDatabaseDataSource.markDirty()

        roomDatabaseDataSource.deleteDirty()
        val result = roomDatabaseDataSource.queryData()

        result.forEach { trendingEntity ->
            trendingEntity.dirty shouldBe false
        }
    }
}

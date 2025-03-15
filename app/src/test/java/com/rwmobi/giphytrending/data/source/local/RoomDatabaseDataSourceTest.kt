/*
 * Copyright (c) 2024-2025. Ryan Wong
 * https://github.com/ryanw-mobile
 */

package com.rwmobi.giphytrending.data.source.local

import android.content.Context
import android.os.Build
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.rwmobi.giphytrending.test.testdata.SampleTrendingEntity
import com.rwmobi.giphytrending.test.testdata.SampleTrendingEntityList
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [Build.VERSION_CODES.P])
internal class RoomDatabaseDataSourceTest {
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

    // Test function names reviewed by Gemini for consistency

    @Test
    fun `returns single entity when single entity is inserted`() = runTest {
        val testData = SampleTrendingEntity.case1
        roomDatabaseDataSource.insertData(data = testData)

        val result = roomDatabaseDataSource.queryData()
        assertEquals(listOf(testData), result)
    }

    @Test
    fun `returns all entities when multiple entities are inserted`() = runTest {
        val testDataList = SampleTrendingEntityList.tripleEntityList
        roomDatabaseDataSource.insertAllData(data = testDataList)

        val result = roomDatabaseDataSource.queryData()
        assertEquals(testDataList.sortedBy { it.id }, result.sortedBy { it.id })
    }

    @Test
    fun `returns empty database when database is cleared after inserting`() = runTest {
        val testDataList = SampleTrendingEntityList.tripleEntityList
        roomDatabaseDataSource.insertAllData(data = testDataList)

        roomDatabaseDataSource.clear()
        val result = roomDatabaseDataSource.queryData()

        assertTrue(result.isEmpty())
    }

    @Test
    fun `sets dirty flag for all entries when markDirty is called after inserting`() = runTest {
        val testDataList = SampleTrendingEntityList.tripleEntityList
        roomDatabaseDataSource.insertAllData(data = testDataList)

        roomDatabaseDataSource.markDirty()
        val result = roomDatabaseDataSource.queryData()

        result.forEach { trendingEntity ->
            assertTrue(trendingEntity.dirty)
        }
    }

    @Test
    fun `removes all dirty entries when data is marked dirty`() = runTest {
        val testDataList = SampleTrendingEntityList.tripleEntityList
        roomDatabaseDataSource.insertAllData(data = testDataList)
        roomDatabaseDataSource.markDirty()

        roomDatabaseDataSource.deleteDirty()
        val result = roomDatabaseDataSource.queryData()

        result.forEach { trendingEntity ->
            assertFalse(trendingEntity.dirty)
        }
    }
}

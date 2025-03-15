/*
 * Copyright (c) 2024. Ryan Wong
 * https://github.com/ryanw-mobile
 */

package com.rwmobi.giphytrending.data.source.local.dao

import android.content.Context
import android.os.Build
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.rwmobi.giphytrending.data.source.local.GiphyDatabase
import com.rwmobi.giphytrending.test.testdata.SampleTrendingEntity
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import kotlin.test.assertContentEquals
import kotlin.test.assertEquals
import kotlin.test.assertTrue

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [Build.VERSION_CODES.P])
internal class TrendingDaoTest {
    private lateinit var giphyDatabase: GiphyDatabase
    private lateinit var trendingDao: TrendingDao

    @Before
    fun setup() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        giphyDatabase = Room.inMemoryDatabaseBuilder(context, GiphyDatabase::class.java)
            .allowMainThreadQueries() // only for testing
            .build()

        trendingDao = giphyDatabase.trendingDao()
    }

    @After
    fun teardown() {
        giphyDatabase.close()
    }

    // Basic test cases: CRUD usages
    // Test function names reviewed by ChatGPT for consistency

    @Test
    fun insertData_WithSingleEntry_ShouldReturnSingleEntry() = runTest {
        trendingDao.insertData(SampleTrendingEntity.case1)
        assertEquals(listOf(SampleTrendingEntity.case1), trendingDao.queryData())
    }

    @Test
    fun insertAllData_WithMultipleEntries_ShouldMatchInsertedEntries() = runTest {
        val testTrendingList = listOf(
            SampleTrendingEntity.case2,
            SampleTrendingEntity.case3,
            SampleTrendingEntity.case4,
        )

        trendingDao.insertAllData(testTrendingList)

        assertEquals(
            testTrendingList.sortedBy { it.id },
            trendingDao.queryData().sortedBy { it.id },
        )
    }

    @Test
    fun updateData_WithModifiedEntry_ShouldReflectUpdatedEntry() = runTest {
        val testTrendingList = listOf(
            SampleTrendingEntity.case1,
            SampleTrendingEntity.case2,
            SampleTrendingEntity.case3,
        )
        val expectedResult = listOf(
            SampleTrendingEntity.case1,
            SampleTrendingEntity.case2Modified,
            SampleTrendingEntity.case3,
        ).sortedBy { it.id }
        trendingDao.insertAllData(testTrendingList)

        trendingDao.insertData(SampleTrendingEntity.case2Modified)

        val trendingList = trendingDao.queryData().sortedBy { it.id }
        assertContentEquals(expectedResult, trendingList)
    }

    @Test
    fun clearDatabase_WhenCalled_ShouldResultInEmptyDatabase() = runTest {
        val testTrendingList = listOf(
            SampleTrendingEntity.case1,
            SampleTrendingEntity.case2,
            SampleTrendingEntity.case3,
        )
        trendingDao.insertAllData(testTrendingList)

        trendingDao.clear()

        assertTrue(trendingDao.queryData().isEmpty())
    }

    // Dirty bit test cases
    @Test
    fun markDirty_OnCleanDatabase_ShouldSetAllEntriesAsDirty() = runTest {
        val testTrendingList = listOf(
            SampleTrendingEntity.case1,
            SampleTrendingEntity.case2,
            SampleTrendingEntity.case3,
        )
        trendingDao.insertAllData(testTrendingList)

        trendingDao.markDirty()

        val trendingList = trendingDao.queryData()
        assertEquals(
            listOf(true, true, true),
            trendingList.map { it.dirty },
        )
    }

    @Test
    fun deleteDirty_WhenAllEntriesAreDirty_ShouldClearDatabase() = runTest {
        val testTrendingList = listOf(
            SampleTrendingEntity.case1,
            SampleTrendingEntity.case2,
            SampleTrendingEntity.case3,
        )
        trendingDao.insertAllData(testTrendingList)
        trendingDao.markDirty()

        trendingDao.deleteDirty()

        assertTrue(trendingDao.queryData().isEmpty())
    }

    @Test
    fun deleteDirty_WithMixedCleanAndDirtyEntries_ShouldRemoveOnlyDirtyEntries() = runTest {
        val testTrendingList = listOf(
            SampleTrendingEntity.case1,
            SampleTrendingEntity.case2,
            SampleTrendingEntity.case3,
        )
        val expectedResult = listOf(SampleTrendingEntity.case4)
        trendingDao.insertAllData(testTrendingList)
        trendingDao.markDirty()
        trendingDao.insertData(SampleTrendingEntity.case4)

        trendingDao.deleteDirty()

        assertEquals(expectedResult, trendingDao.queryData())
    }
}

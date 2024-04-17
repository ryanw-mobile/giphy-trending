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
import io.kotest.matchers.collections.shouldContain
import io.kotest.matchers.collections.shouldContainAll
import io.kotest.matchers.collections.shouldContainExactly
import io.kotest.matchers.collections.shouldHaveSize
import io.kotest.matchers.collections.shouldNotContain
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
class TrendingDaoTest {
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
    @Test
    fun emptyDatabase_InsertOne_ReturnOne() =
        runTest {
            trendingDao.queryData() shouldHaveSize 0

            trendingDao.insertData(SampleTrendingEntity.case1)

            trendingDao.queryData() shouldContainExactly (listOf(SampleTrendingEntity.case1))
        }

    @Test
    fun emptyDatabase_InsertList_ReturnSameList() =
        runTest {
            trendingDao.queryData() shouldHaveSize 0

            val testTrendingList = listOf(
                SampleTrendingEntity.case2,
                SampleTrendingEntity.case3,
                SampleTrendingEntity.case4,
            )
            trendingDao.insertAllData(testTrendingList)

            // Note: The order may not be the same due to sorting
            trendingDao.queryData() shouldContainAll (testTrendingList)
        }

    @Test
    fun nonEmptyDatabase_UpdateOneRow_ReturnUpdatedList() =
        runTest {
            val testTrendingList = listOf(
                SampleTrendingEntity.case1,
                SampleTrendingEntity.case2,
                SampleTrendingEntity.case3,
            )
            trendingDao.insertAllData(testTrendingList)
            trendingDao.queryData() shouldHaveSize 3

            trendingDao.insertData(SampleTrendingEntity.case2Modified)

            val trendingList = trendingDao.queryData()
            trendingList shouldHaveSize 3
            trendingList shouldContain SampleTrendingEntity.case1
            trendingList shouldNotContain SampleTrendingEntity.case2
            trendingList shouldContain SampleTrendingEntity.case2Modified
            trendingList shouldContain SampleTrendingEntity.case3
        }

    @Test
    fun nonemptyDatabase_ClearDatabase_ReturnEmptyList() =
        runTest {
            val testTrendingList = listOf(
                SampleTrendingEntity.case1,
                SampleTrendingEntity.case2,
                SampleTrendingEntity.case3,
            )
            trendingDao.insertAllData(testTrendingList)
            trendingDao.queryData() shouldHaveSize 3

            trendingDao.clear()

            trendingDao.queryData() shouldHaveSize 0
        }

    /***
     * Dirty bit test cases
     */
    @Test
    fun nonEmptyCleanDatabase_markDirty_ReturnAllDirty() =
        runTest {
            val testTrendingList = listOf(
                SampleTrendingEntity.case1,
                SampleTrendingEntity.case2,
                SampleTrendingEntity.case3,
            )
            trendingDao.insertAllData(testTrendingList)
            val initialList = trendingDao.queryData()
            initialList shouldHaveSize 3
            initialList[0].dirty shouldBe false
            initialList[1].dirty shouldBe false
            initialList[2].dirty shouldBe false

            trendingDao.markDirty()

            val trendingList = trendingDao.queryData()
            trendingList shouldHaveSize 3
            trendingList[0].dirty shouldBe true
            trendingList[1].dirty shouldBe true
            trendingList[2].dirty shouldBe true
        }

    @Test
    fun allDirtyDatabase_deleteDirty_ReturnEmpty() =
        runTest {
            val testTrendingList = listOf(
                SampleTrendingEntity.case1,
                SampleTrendingEntity.case2,
                SampleTrendingEntity.case3,
            )
            trendingDao.insertAllData(testTrendingList)
            trendingDao.markDirty()
            val initialList = trendingDao.queryData()
            initialList shouldHaveSize 3
            initialList[0].dirty shouldBe true
            initialList[1].dirty shouldBe true
            initialList[2].dirty shouldBe true

            trendingDao.deleteDirty()

            trendingDao.queryData() shouldHaveSize 0
        }

    @Test
    fun someDirtyDatabase_deleteDirty_ReturnClean() =
        runTest {
            val testTrendingList = listOf(
                SampleTrendingEntity.case1,
                SampleTrendingEntity.case2,
                SampleTrendingEntity.case3,
            )
            trendingDao.insertAllData(testTrendingList)
            trendingDao.markDirty()
            trendingDao.insertData(SampleTrendingEntity.case4)
            trendingDao.queryData() shouldHaveSize 4

            trendingDao.deleteDirty()

            trendingDao.queryData() shouldContainExactly (listOf(SampleTrendingEntity.case4))
        }
}

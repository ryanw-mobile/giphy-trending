package uk.ryanwong.giphytrending.data.source.local

import android.content.Context
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.MediumTest
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.MatcherAssert
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
@MediumTest
class TrendingDaoTest {
    @get:Rule
    var instantTaskExecutorRule =
        InstantTaskExecutorRule() //it's just for Junit to execute tasks synchronously
    private lateinit var giphyDatabase: GiphyDatabase
    private lateinit var trendingDao: TrendingDao

    @Before
    fun init() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        giphyDatabase = Room.inMemoryDatabaseBuilder(context, GiphyDatabase::class.java)
            .allowMainThreadQueries() //only for testing
            .build()

        trendingDao = giphyDatabase.trendingDao()
    }

    // Basic test cases: CRUD usages
    @Test
    fun emptyDatabase_InsertOne_ReturnOne() {
        // Given: Empty database -- Do nothing
        trendingDao.queryData().test().assertValue {
            it.isEmpty()
        }
        
        // When: Insert one TrendingEntity
        trendingDao.insertData(TrendingEntityTestData.case1)

        // Then: Database should have one row returned
        val trendingList = trendingDao.queryData().blockingGet()
        MatcherAssert.assertThat(trendingList.size, `is`(1))
        MatcherAssert.assertThat(trendingList[0] == TrendingEntityTestData.case1, `is`(true))
    }

    @Test
    fun emptyDatabase_InsertList_ReturnSameList() {
        // Given: Empty database -- Do nothing
        // RxJava way
        trendingDao.queryData().test().assertValue {
            it.isEmpty()
        }

        // When: Insert a list of 3 TrendingEntity objects
        val testTrendingList = listOf(
            TrendingEntityTestData.case2,
            TrendingEntityTestData.case3,
            TrendingEntityTestData.case4
        )
        trendingDao.insertAllData(testTrendingList)

        // Then: Database should have three row returned
        // Note: The order may not be the same due to sorting
        val trendingList = trendingDao.queryData().blockingGet()
        MatcherAssert.assertThat(trendingList.size, `is`(3))
        MatcherAssert.assertThat(trendingList.containsAll(testTrendingList), `is`(true))
    }

    @Test
    fun nonEmptyDatabase_UpdateOneRow_ReturnUpdatedList() {
        // Given: database  with three TrendingEntity objects
        val testTrendingList = listOf(
            TrendingEntityTestData.case1,
            TrendingEntityTestData.case2,
            TrendingEntityTestData.case3
        )
        trendingDao.insertAllData(testTrendingList)
        trendingDao.queryData().test().assertValue {
            it.size == 3
        }

        // When: insert a modified version of case 2 with same ID
        trendingDao.insertData(TrendingEntityTestData.case2Modified)

        // Then: Database should have three row returned
        // containing case1, case2Modified, and case 3
        val trendingList = trendingDao.queryData().blockingGet()
        MatcherAssert.assertThat(trendingList.size, `is`(3))
        MatcherAssert.assertThat(trendingList.containsAll(testTrendingList), `is`(false))
        MatcherAssert.assertThat(trendingList.contains(TrendingEntityTestData.case1), `is`(true))
        MatcherAssert.assertThat(trendingList.contains(TrendingEntityTestData.case2), `is`(false))
        MatcherAssert.assertThat(trendingList.contains(TrendingEntityTestData.case3), `is`(true))
        MatcherAssert.assertThat(
            trendingList.contains(TrendingEntityTestData.case2Modified),
            `is`(true)
        )
    }

    @Test
    fun nonemptyDatabase_ClearDatabase_ReturnEmptyList() {
        // Given: database  with three TrendingEntity objects
        val testTrendingList = listOf(
            TrendingEntityTestData.case1,
            TrendingEntityTestData.case2,
            TrendingEntityTestData.case3
        )
        trendingDao.insertAllData(testTrendingList)
        trendingDao.queryData().test().assertValue {
            it.size == 3
        }

        // When: Clear the database
        trendingDao.clear()

        // Then: Database should have zero rows returned
        // JUnit / Hamcrest's way
        // val trendingList = trendingDao.queryData().blockingGet()
        // MatcherAssert.assertThat(trendingList.size, `is`(0))

        // RxJava way
        trendingDao.queryData().test().assertValue {
            it.isEmpty()
        }
    }

    /***
     * Dirty bit test cases
     */
    @Test
    fun nonEmptyCleanDatabase_markDirty_ReturnAllDirty() {
        // Given: database with three TrendingEntity objects, all dirty = false
        val testTrendingList = listOf(
            TrendingEntityTestData.case1,
            TrendingEntityTestData.case2,
            TrendingEntityTestData.case3
        )
        trendingDao.insertAllData(testTrendingList)
        val trendingListGiven = trendingDao.queryData().blockingGet()
        MatcherAssert.assertThat(trendingListGiven.size, `is`(3))
        MatcherAssert.assertThat(trendingListGiven[0].dirty, `is`(false))
        MatcherAssert.assertThat(trendingListGiven[1].dirty, `is`(false))
        MatcherAssert.assertThat(trendingListGiven[2].dirty, `is`(false))

        // When: mark database dirty
        trendingDao.markDirty()

        // Then: Database should have all rows marked dirty
        val trendingList = trendingDao.queryData().blockingGet()
        MatcherAssert.assertThat(trendingList.size, `is`(3))
        MatcherAssert.assertThat(trendingList[0].dirty, `is`(true))
        MatcherAssert.assertThat(trendingList[1].dirty, `is`(true))
        MatcherAssert.assertThat(trendingList[2].dirty, `is`(true))
    }

    @Test
    fun allDirtyDatabase_deleteDirty_ReturnEmpty() {
        // Given: database with three TrendingEntity objects, all dirty = true
        val testTrendingList = listOf(
            TrendingEntityTestData.case1,
            TrendingEntityTestData.case2,
            TrendingEntityTestData.case3
        )
        trendingDao.insertAllData(testTrendingList)
        trendingDao.markDirty()
        val trendingListGiven = trendingDao.queryData().blockingGet()
        MatcherAssert.assertThat(trendingListGiven.size, `is`(3))
        MatcherAssert.assertThat(trendingListGiven[0].dirty, `is`(true))
        MatcherAssert.assertThat(trendingListGiven[1].dirty, `is`(true))
        MatcherAssert.assertThat(trendingListGiven[2].dirty, `is`(true))

        // When: delete dirty rows
        trendingDao.deleteDirty()

        // Then: Database should return empty list
        // JUnit / Hamcrest way
        // val trendingList = trendingDao.queryData().blockingGet()
        // MatcherAssert.assertThat(trendingList.size, `is`(0))

        // RxJava way
        trendingDao.queryData().test().assertValue {
            it.isEmpty()
        }
    }

    @Test
    fun someDirtyDatabase_deleteDirty_ReturnClean() {
        // Given: database with 4 TrendingEntity objects - 3 dirty and 1 clean
        val testTrendingList = listOf(
            TrendingEntityTestData.case1,
            TrendingEntityTestData.case2,
            TrendingEntityTestData.case3
        )
        trendingDao.insertAllData(testTrendingList)
        trendingDao.markDirty()
        trendingDao.insertData(TrendingEntityTestData.case4)

        trendingDao.queryData().test().assertValue {
            it.size == 4
        }

        // When: delete dirty rows
        trendingDao.deleteDirty()

        // Then: Database should remain 1 clean row
        val trendingList = trendingDao.queryData().blockingGet()
        MatcherAssert.assertThat(trendingList.size, `is`(1))
        MatcherAssert.assertThat(trendingList[0] == TrendingEntityTestData.case4, `is`(true))
    }

    @After
    fun cleanUp() {
        giphyDatabase.close()
    }
}
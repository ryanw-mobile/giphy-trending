package uk.ryanwong.giphytrending.data.source.local

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.MediumTest
import io.kotest.matchers.collections.shouldContain
import io.kotest.matchers.collections.shouldContainAll
import io.kotest.matchers.collections.shouldContainExactly
import io.kotest.matchers.collections.shouldHaveSize
import io.kotest.matchers.collections.shouldNotContain
import io.kotest.matchers.shouldBe
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
@MediumTest
class TrendingDaoTest {
    private lateinit var giphyDatabase: GiphyDatabase
    private lateinit var trendingDao: TrendingDao

    @Before
    fun init() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        giphyDatabase = Room.inMemoryDatabaseBuilder(context, GiphyDatabase::class.java)
            .allowMainThreadQueries() // only for testing
            .build()

        trendingDao = giphyDatabase.trendingDao()
    }

    // Basic test cases: CRUD usages
    @Test
    fun emptyDatabase_InsertOne_ReturnOne() {
        runBlocking {
            // 🔴 Given: Empty database
            trendingDao.queryData() shouldHaveSize 0

            // 🟡 When: Insert one TrendingEntity
            trendingDao.insertData(TrendingEntityTestData.case1)

            // 🟢 Then: Database should have one row returned
            trendingDao.queryData() shouldContainExactly (listOf(TrendingEntityTestData.case1))
        }
    }

    @Test
    fun emptyDatabase_InsertList_ReturnSameList() {
        runBlocking {
            // 🔴 Given: Empty database
            trendingDao.queryData() shouldHaveSize 0

            // 🟡 When: Insert a list of 3 TrendingEntity objects
            val testTrendingList = listOf(
                TrendingEntityTestData.case2,
                TrendingEntityTestData.case3,
                TrendingEntityTestData.case4
            )
            trendingDao.insertAllData(testTrendingList)

            // 🟢 Then: Database should have three row returned
            // Note: The order may not be the same due to sorting
            trendingDao.queryData() shouldContainAll (testTrendingList)
        }
    }

    @Test
    fun nonEmptyDatabase_UpdateOneRow_ReturnUpdatedList() {
        runBlocking {
            // 🔴 Given: database with three TrendingEntity objects
            val testTrendingList = listOf(
                TrendingEntityTestData.case1,
                TrendingEntityTestData.case2,
                TrendingEntityTestData.case3
            )
            trendingDao.insertAllData(testTrendingList)
            trendingDao.queryData() shouldHaveSize 3

            // 🟡 When: insert a modified version of case 2 with same ID
            trendingDao.insertData(TrendingEntityTestData.case2Modified)

            // 🟢 Then: Database should have three row returned
            // containing case1, case2Modified, and case 3
            val trendingList = trendingDao.queryData()
            trendingList shouldHaveSize 3
            trendingList shouldContain TrendingEntityTestData.case1
            trendingList shouldNotContain TrendingEntityTestData.case2
            trendingList shouldContain TrendingEntityTestData.case2Modified
            trendingList shouldContain TrendingEntityTestData.case3
        }
    }

    @Test
    fun nonemptyDatabase_ClearDatabase_ReturnEmptyList() {
        runBlocking {
            // 🔴 Given: database with three TrendingEntity objects
            val testTrendingList = listOf(
                TrendingEntityTestData.case1,
                TrendingEntityTestData.case2,
                TrendingEntityTestData.case3
            )
            trendingDao.insertAllData(testTrendingList)
            trendingDao.queryData() shouldHaveSize 3

            // 🟡 When: Clear the database
            trendingDao.clear()

            // 🟢 Then: Database should have zero rows returned
            trendingDao.queryData() shouldHaveSize 0
        }
    }

    /***
     * Dirty bit test cases
     */
    @Test
    fun nonEmptyCleanDatabase_markDirty_ReturnAllDirty() {
        runBlocking {
            // 🔴 Given: database with three TrendingEntity objects, all dirty = false
            val testTrendingList = listOf(
                TrendingEntityTestData.case1,
                TrendingEntityTestData.case2,
                TrendingEntityTestData.case3
            )
            trendingDao.insertAllData(testTrendingList)
            val initialList = trendingDao.queryData()
            initialList shouldHaveSize 3
            initialList[0].dirty shouldBe false
            initialList[1].dirty shouldBe false
            initialList[2].dirty shouldBe false

            // 🟡 When: mark database dirty
            trendingDao.markDirty()

            // 🟢 Then: Database should have all rows marked dirty
            val trendingList = trendingDao.queryData()
            trendingList shouldHaveSize 3
            trendingList[0].dirty shouldBe true
            trendingList[1].dirty shouldBe true
            trendingList[2].dirty shouldBe true
        }
    }

    @Test
    fun allDirtyDatabase_deleteDirty_ReturnEmpty() {
        runBlocking {
            // 🔴 Given: database with three TrendingEntity objects, all dirty = true
            val testTrendingList = listOf(
                TrendingEntityTestData.case1,
                TrendingEntityTestData.case2,
                TrendingEntityTestData.case3
            )
            trendingDao.insertAllData(testTrendingList)
            trendingDao.markDirty()
            val initialList = trendingDao.queryData()
            initialList shouldHaveSize 3
            initialList[0].dirty shouldBe true
            initialList[1].dirty shouldBe true
            initialList[2].dirty shouldBe true

            // 🟡 When: delete dirty rows
            trendingDao.deleteDirty()

            // 🟢 Then: Database should return empty list
            trendingDao.queryData() shouldHaveSize 0
        }
    }

    @Test
    fun someDirtyDatabase_deleteDirty_ReturnClean() {
        runBlocking {
            // 🔴 Given: database with 4 TrendingEntity objects - 3 dirty and 1 clean
            val testTrendingList = listOf(
                TrendingEntityTestData.case1,
                TrendingEntityTestData.case2,
                TrendingEntityTestData.case3
            )
            trendingDao.insertAllData(testTrendingList)
            trendingDao.markDirty()
            trendingDao.insertData(TrendingEntityTestData.case4)
            trendingDao.queryData() shouldHaveSize 4

            // 🟡 When: delete dirty rows
            trendingDao.deleteDirty()

            // 🟢 Then: Database should remain 1 clean row
            trendingDao.queryData() shouldContainExactly (listOf(TrendingEntityTestData.case4))
        }
    }

    @After
    fun cleanUp() {
        giphyDatabase.close()
    }
}

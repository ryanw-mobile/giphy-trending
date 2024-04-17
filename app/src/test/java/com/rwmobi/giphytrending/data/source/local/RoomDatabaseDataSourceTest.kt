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
            .allowMainThreadQueries() // only for testing
            .build()

        roomDatabaseDataSource = RoomDatabaseDataSource(giphyDatabase = giphyDatabase)
    }

    @After
    fun teardown() {
        giphyDatabase.close()
    }

    @Test
    fun `Given a single TrendingEntity, when inserting into the database, then it should be retrievable`() = runTest {
        val testData = SampleTrendingEntity.case1
        roomDatabaseDataSource.insertData(data = testData)

        val result = roomDatabaseDataSource.queryData()

        result.size shouldBe 1
        result[0] shouldBe testData
    }

    @Test
    fun `Given a list of TrendingEntity, when inserting all into the database, then all should be retrievable`() = runTest {
        val testDataList = SampleTrendingEntityList.tripleEntityList
        roomDatabaseDataSource.insertAllData(data = testDataList)

        val result = roomDatabaseDataSource.queryData()

        result.size shouldBe testDataList.size
        result shouldContainOnly testDataList
    }

    @Test
    fun `Given data in the database, when clearing, then it should be empty`() = runTest {
        // Given
        val testDataList = SampleTrendingEntityList.tripleEntityList
        roomDatabaseDataSource.insertAllData(data = testDataList)

        // When
        roomDatabaseDataSource.clear()
        val result = roomDatabaseDataSource.queryData()

        // Then
        result.size shouldBe 0
    }

    @Test
    fun `Given data in the database, when marking as dirty, then dirty flag should be set`() = runTest {
        // Given
        val testDataList = SampleTrendingEntityList.tripleEntityList
        roomDatabaseDataSource.insertAllData(data = testDataList)

        // When
        roomDatabaseDataSource.markDirty()
        val result = roomDatabaseDataSource.queryData()

        // Then
        result.forEach { trendingEntity ->
            trendingEntity.dirty shouldBe true
        }
    }

    @Test
    fun `Given dirty data in the database, when deleting, then no dirty data should remain`() = runTest {
        // Given
        val testDataList = SampleTrendingEntityList.tripleEntityList
        roomDatabaseDataSource.insertAllData(data = testDataList)
        roomDatabaseDataSource.markDirty()

        // When
        roomDatabaseDataSource.deleteDirty()
        val result = roomDatabaseDataSource.queryData()

        // Then
        result.forEach { trendingEntity ->
            trendingEntity.dirty shouldBe false
        }
    }
}

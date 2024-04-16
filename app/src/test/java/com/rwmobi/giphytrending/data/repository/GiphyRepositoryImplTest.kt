import com.rwmobi.giphytrending.data.repository.GiphyRepositoryImpl
import com.rwmobi.giphytrending.data.source.local.FakeRoomDbDataSource
import com.rwmobi.giphytrending.data.source.local.toDomainModelList
import com.rwmobi.giphytrending.data.source.network.FakeNetworkDataSource
import com.rwmobi.giphytrending.domain.exceptions.EmptyGiphyAPIKeyException
import com.rwmobi.giphytrending.domain.model.Rating
import com.rwmobi.giphytrending.domain.repository.GiphyRepository
import com.rwmobi.giphytrending.test.testdata.SampleTrendingEntityList
import com.rwmobi.giphytrending.test.testdata.SampleTrendingNetworkResponse
import io.kotest.matchers.shouldBe
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestDispatcher
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Test

@ExperimentalCoroutinesApi
class GiphyRepositoryImplTest {
    private lateinit var giphyRepository: GiphyRepository
    private lateinit var dispatcher: TestDispatcher
    private lateinit var fakeRoomDbDataSource: FakeRoomDbDataSource
    private lateinit var fakeNetworkDataSource: FakeNetworkDataSource

    private fun setupRepository(giphyApiKey: String = "some-api-key") {
        dispatcher = UnconfinedTestDispatcher()
        fakeRoomDbDataSource = FakeRoomDbDataSource()
        fakeNetworkDataSource = FakeNetworkDataSource()
        giphyRepository = GiphyRepositoryImpl(
            networkDataSource = fakeNetworkDataSource,
            roomDbDataSource = fakeRoomDbDataSource,
            giphyApiKey = giphyApiKey,
            dispatcher = dispatcher,
        )
    }

    @Test
    fun `fetchCachedTrending should return correct list if database query success`() = runTest {
        setupRepository()
        fakeRoomDbDataSource.mockQueryDataResponse = SampleTrendingEntityList.jobBidenEntity

        val result = giphyRepository.fetchCachedTrending()

        result.isSuccess shouldBe true
        result.getOrNull() shouldBe SampleTrendingEntityList.jobBidenEntity.toDomainModelList()
    }

    @Test
    fun `fetchCachedTrending should return failure if database query throws exception`() = runTest {
        setupRepository()
        fakeRoomDbDataSource.apiError = Exception()

        val result = giphyRepository.fetchCachedTrending()

        result.isFailure shouldBe true
        result.exceptionOrNull() shouldBe Exception()
    }

    @Test
    fun `reloadTrending should return correct list if network and database operations all success`() = runTest {
        setupRepository()
        fakeNetworkDataSource.mockTrendingNetworkResponse = SampleTrendingNetworkResponse.jobBidenResponse
        fakeRoomDbDataSource.mockQueryDataResponse = SampleTrendingEntityList.jobBidenEntity

        val result = giphyRepository.reloadTrending(limit = 100, rating = Rating.R)

        result.isSuccess shouldBe true
        result.getOrNull() shouldBe SampleTrendingEntityList.jobBidenEntity.toDomainModelList()
    }

    @Test
    fun `reloadTrending should return failure if giphyApiKey is blank`() = runTest {
        setupRepository(giphyApiKey = "")
        fakeNetworkDataSource.apiError = Exception()

        val result = giphyRepository.reloadTrending(limit = 100, rating = Rating.R)

        result.isFailure shouldBe true
        result.exceptionOrNull() shouldBe EmptyGiphyAPIKeyException()
    }

    @Test
    fun `reloadTrending should return failure if network call returns an error`() = runTest {
        setupRepository()
        fakeNetworkDataSource.apiError = Exception()

        val result = giphyRepository.reloadTrending(limit = 100, rating = Rating.R)

        result.isFailure shouldBe true
        result.exceptionOrNull() shouldBe Exception()
    }

    @Test
    fun `reloadTrending should return failure if database operation throws exception`() = runTest {
        setupRepository()
        fakeRoomDbDataSource.apiError = Exception()

        val result = giphyRepository.reloadTrending(limit = 100, rating = Rating.R)

        result.isFailure shouldBe true
        result.exceptionOrNull() shouldBe Exception()
    }
}

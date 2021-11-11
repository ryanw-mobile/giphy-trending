package uk.ryanwong.giphytrending.data.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import io.reactivex.Completable
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import io.reactivex.subscribers.DisposableSubscriber
import uk.ryanwong.giphytrending.BuildConfig
import uk.ryanwong.giphytrending.GiphyApplication
import uk.ryanwong.giphytrending.data.source.local.toDomainModelList
import uk.ryanwong.giphytrending.data.source.local.toTrendingEntityList
import uk.ryanwong.giphytrending.data.source.network.GiphyApi
import uk.ryanwong.giphytrending.data.source.network.model.TrendingNetworkModel
import uk.ryanwong.giphytrending.di.DaggerAppComponent
import uk.ryanwong.giphytrending.domain.model.TrendingDomainModel
import javax.inject.Inject

class TrendingRepository {

    @Inject
    lateinit var giphyApiService: GiphyApi

    private val _trendingList by lazy { MutableLiveData<List<TrendingDomainModel>>() }
    val trendingList: LiveData<List<TrendingDomainModel>>
        get() = _trendingList

    private val _isInProgress by lazy { MutableLiveData<Boolean>() }
    val isInProgress: LiveData<Boolean>
        get() = _isInProgress

    private val _isError by lazy { MutableLiveData<String>() }
    val isError: LiveData<String>
        get() = _isError

    init {
        DaggerAppComponent.create().inject(this)
    }

    /***
     * Fetch and expose trending list
     * Repository currently retrieves cached data from local database.
     * This can be changed without acknowledging to the callers.
     */
    fun fetchTrending(): Disposable {
        return GiphyApplication.database.trendingDao()
            .queryData()
            .subscribeOn(Schedulers.io())
            .subscribe(
                { dataEntityList ->
                    if (dataEntityList != null) {
                        _isError.postValue(null)
                        _trendingList.postValue(dataEntityList.toDomainModelList())
                    }
                },
                {
                    Log.e("TrendingRepository", "Database error: ${it.message}")
                    _isError.postValue(
                        it?.message ?: "Error when retrieving data from the local storage."
                    )
                }
            )
    }

    /***
     * Request the repository to refresh and update the exposed trending list
     * Repository currently run RestAPI calls, cache data to local database,
     * and return the cached contents.
     */
    fun refreshTrending(): Disposable {
        return Completable.fromAction {
            // Mark existing contents dirty. After successful API call old entries will be removed
            GiphyApplication.database.trendingDao().markDirty()
        }.subscribeOn(Schedulers.io())
            .subscribe(
                {
                    Log.v("TrendingRepository", "refreshTrending() - mark dirty: success")
                    getTrendingFromNetwork()
                }, {
                    Log.e(
                        "TrendingRepository",
                        "refreshTrending() -Database error when marking dirty bit: ${it.message}"
                    )
                    _isError.postValue(it?.message ?: "Error when accessing to the local storage.")
                }
            )
    }

    private fun getTrendingFromNetwork(): Disposable {
        return giphyApiService.getTrending(
            BuildConfig.GIPHY_API_KEY, BuildConfig.API_LIMIT,
            BuildConfig.API_RATING
        )
            .subscribeOn(Schedulers.io())
            .subscribeWith(cacheTrendingToDb())
    }

    private fun cacheTrendingToDb(): DisposableSubscriber<TrendingNetworkModel> {
        return object : DisposableSubscriber<TrendingNetworkModel>() {
            override fun onNext(trending: TrendingNetworkModel?) {
                if (trending != null) {
                    val dataList = trending.trendingData.toTrendingEntityList()
                    GiphyApplication.database.trendingDao().insertAllData(dataList)
                }
            }

            override fun onError(t: Throwable?) {
                _isInProgress.postValue(true)
                Log.e("TrendingRepository", "cacheTrendingToDb(): error - ${t?.message}")
                _isError.postValue(
                    t?.message ?: "Error when processing data returned from the server."
                )
                _isInProgress.postValue(false)
            }

            override fun onComplete() {
                Log.v("TrendingRepository", "cacheTrendingToDb(): insertion completed")
                invalidateDirtyTrendingDb()
            }
        }
    }

    private fun invalidateDirtyTrendingDb(): Disposable {
        return Completable.fromAction {
            GiphyApplication.database.trendingDao().deleteDirty()
        }.subscribeOn(Schedulers.io())
            .subscribe({
                Log.v("TrendingRepository", "invalidateDirtyTrendingDb(): success")
                fetchTrending()
            },
                {
                    Log.e(
                        "TrendingRepository",
                        "invalidateDirtyTrendingDb: error when invalidating dirty rows - ${it?.message}"
                    )
                })
    }
}
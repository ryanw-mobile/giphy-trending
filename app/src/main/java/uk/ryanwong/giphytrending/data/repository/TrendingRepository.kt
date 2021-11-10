package uk.ryanwong.giphytrending.data.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import io.reactivex.android.schedulers.AndroidSchedulers
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

    fun fetchDataFromDatabase(): Disposable = getTrendingQuery()

    private fun insertData(): Disposable {
        return giphyApiService.getTrending(
            BuildConfig.GIPHY_API_KEY, BuildConfig.API_LIMIT,
            BuildConfig.API_RATING
        )
            .subscribeOn(Schedulers.io())
            .subscribeWith(subscribeToDatabase())
    }

    private fun subscribeToDatabase(): DisposableSubscriber<TrendingNetworkModel> {
        return object : DisposableSubscriber<TrendingNetworkModel>() {

            override fun onNext(trending: TrendingNetworkModel?) {
                if (trending != null) {
                    val dataList = trending.trendingData.toTrendingEntityList()
                    GiphyApplication.database.dataDao().insertAllData(dataList)
                }
            }

            override fun onError(t: Throwable?) {
                _isInProgress.postValue(true)
                Log.e("insertData()", "TrendingResult error: ${t?.message}")
                _isError.postValue(t?.message ?: "Unknown Error")
                _isInProgress.postValue(false)
            }

            override fun onComplete() {
                Log.v("insertData()", "insert success")
                getTrendingQuery()
            }
        }
    }

    private fun getTrendingQuery(): Disposable {
        return GiphyApplication.database.dataDao()
            .queryData()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { dataEntityList ->
                    _isInProgress.postValue(true)
                    if (dataEntityList != null && dataEntityList.isNotEmpty()) {
                        _isError.postValue(null)
                        _trendingList.postValue(dataEntityList.toDomainModelList())
                    } else {
                        insertData()
                    }
                    _isInProgress.postValue(false)

                },
                {
                    _isInProgress.postValue(true)
                    Log.e("getTrendingQuery()", "Database error: ${it.message}")
                    _isError.postValue(it?.message ?: "Database error")
                    _isInProgress.postValue(false)
                }
            )
    }
}
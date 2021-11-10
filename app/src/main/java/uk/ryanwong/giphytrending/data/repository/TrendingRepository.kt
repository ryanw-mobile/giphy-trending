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
import uk.ryanwong.giphytrending.data.source.local.toDataEntityList
import uk.ryanwong.giphytrending.data.source.local.toDataList
import uk.ryanwong.giphytrending.data.source.network.GiphyApi
import uk.ryanwong.giphytrending.di.DaggerAppComponent
import uk.ryanwong.giphytrending.model.Data
import uk.ryanwong.giphytrending.model.Trending
import javax.inject.Inject

class TrendingRepository {

    @Inject
    lateinit var giphyApiService: GiphyApi

    private val _data by lazy { MutableLiveData<List<Data>>() }
    val data: LiveData<List<Data>>
        get() = _data

    private val _isInProgress by lazy { MutableLiveData<Boolean>() }
    val isInProgress: LiveData<Boolean>
        get() = _isInProgress

    private val _isError by lazy { MutableLiveData<Boolean>() }
    val isError: LiveData<Boolean>
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

    private fun subscribeToDatabase(): DisposableSubscriber<Trending> {
        return object : DisposableSubscriber<Trending>() {

            override fun onNext(trending: Trending?) {
                if (trending != null) {
                    val entityList = trending.data.toList().toDataEntityList()
                    GiphyApplication.database.apply {
                        dataDao().insertData(entityList)
                    }
                }
            }

            override fun onError(t: Throwable?) {
                _isInProgress.postValue(true)
                Log.e("insertData()", "TrendingResult error: ${t?.message}")
                _isError.postValue(true)
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
                        _isError.postValue(false)
                        _data.postValue(dataEntityList.toDataList())
                    } else {
                        insertData()
                    }
                    _isInProgress.postValue(false)

                },
                {
                    _isInProgress.postValue(true)
                    Log.e("getTrendingQuery()", "Database error: ${it.message}")
                    _isError.postValue(true)
                    _isInProgress.postValue(false)
                }
            )
    }
}
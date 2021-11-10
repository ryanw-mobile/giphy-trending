package uk.ryanwong.giphytrending.data.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import uk.ryanwong.giphytrending.data.source.network.GiphyApi
import uk.ryanwong.giphytrending.di.DaggerAppComponent
import uk.ryanwong.giphytrending.model.Data
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

}
package uk.ryanwong.giphytrending.data.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import uk.ryanwong.giphytrending.data.repository.GiphyRepository
import uk.ryanwong.giphytrending.data.repository.GiphyRepositoryImpl
import uk.ryanwong.giphytrending.data.source.local.GiphyDatabase
import uk.ryanwong.giphytrending.data.source.network.GiphyApi
import uk.ryanwong.giphytrending.di.IoDispatcher
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class GiphyRepositoryModule {
    @Singleton
    @Provides
    fun provideGiphyRepository(
        giphyApiService: GiphyApi,
        giphyDatabase: GiphyDatabase,
        @IoDispatcher dispatcher: CoroutineDispatcher,
    ): GiphyRepository {
        return GiphyRepositoryImpl(
            giphyApiService = giphyApiService,
            giphyDatabase = giphyDatabase,
            dispatcher = dispatcher
        )
    }
}

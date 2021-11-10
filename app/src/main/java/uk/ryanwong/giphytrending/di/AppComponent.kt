package uk.ryanwong.giphytrending.di

import dagger.Component
import uk.ryanwong.giphytrending.data.repository.TrendingRepository
import javax.inject.Singleton

@Singleton
@Component(modules = [AppModule::class])
interface AppComponent {

    fun inject(trendingRepository: TrendingRepository)
}
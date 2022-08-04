package uk.ryanwong.giphytrending.data.source.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import dagger.hilt.android.components.FragmentComponent
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ActivityScoped
import dagger.hilt.android.scopes.FragmentScoped
import dagger.hilt.android.scopes.ViewModelScoped
import dagger.hilt.components.SingletonComponent
import uk.ryanwong.giphytrending.ui.GiphyImageItemAdapter

@Module
@InstallIn(FragmentComponent::class)
object GiphyImageItemAdapterModule {
    @FragmentScoped
    @Provides
    fun provideGiphyImageItemAdapter(): GiphyImageItemAdapter {
        return GiphyImageItemAdapter().apply {
            setHasStableIds(true)
        }
    }
}
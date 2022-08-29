package uk.ryanwong.giphytrending.data.source.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.FragmentComponent
import dagger.hilt.android.scopes.FragmentScoped
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

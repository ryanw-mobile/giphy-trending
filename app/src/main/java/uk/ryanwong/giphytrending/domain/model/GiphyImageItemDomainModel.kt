package uk.ryanwong.giphytrending.domain.model

data class GiphyImageItemDomainModel(
    val id: String,
    val previewUrl: String,
    val imageUrl: String,
    val webUrl: String,
    val title: String,
    val type: String,
    val username: String
)
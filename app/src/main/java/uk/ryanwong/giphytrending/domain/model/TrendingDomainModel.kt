package uk.ryanwong.giphytrending.domain.model

data class TrendingDomainModel(
    var id: String,
    val url: String,
    val title: String,
    val type: String,
    val username: String
)
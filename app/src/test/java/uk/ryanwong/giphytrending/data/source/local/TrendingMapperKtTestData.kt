package uk.ryanwong.giphytrending.data.source.local

import uk.ryanwong.giphytrending.domain.model.GiphyImageItemDomainModel
import java.util.Date

object TrendingMapperKtTestData {

    val mockTrendingEntity1 = TrendingEntity(
        id = "5KF7hci72bv2ZbchuT",
        previewUrl = "https://media2.giphy.com/media/5KF7hci72bv2ZbchuT/200w.gif",
        imageUrl = "https://media2.giphy.com/media/5KF7hci72bv2ZbchuT/giphy.gif",
        webUrl = "https://giphy.com/gifs/studiosoriginals-cooking-chef-cook-5KF7hci72bv2ZbchuT",
        title = "Chef Cooking GIF by GIPHY Studios Originals",
        type = "gif",
        username = "studiosoriginals",
        trendingDateTime = Date(1636679711000),
        importDateTime = Date(1635978358000)
    )

    val mockDomainModel1 = GiphyImageItemDomainModel(
        id = "5KF7hci72bv2ZbchuT",
        previewUrl = "https://media2.giphy.com/media/5KF7hci72bv2ZbchuT/200w.gif",
        imageUrl = "https://media2.giphy.com/media/5KF7hci72bv2ZbchuT/giphy.gif",
        webUrl = "https://giphy.com/gifs/studiosoriginals-cooking-chef-cook-5KF7hci72bv2ZbchuT",
        title = "Chef Cooking GIF by GIPHY Studios Originals",
        type = "gif",
        username = "studiosoriginals"
    )

    val mockTrendingEntity2 = TrendingEntity(
        id = "uaIAIw3ELuk69mhZ5I",
        previewUrl = "https://media3.giphy.com/media/uaIAIw3ELuk69mhZ5I/200w.gif",
        imageUrl = "https://media3.giphy.com/media/uaIAIw3ELuk69mhZ5I/giphy.gif",
        webUrl = "https://giphy.com/gifs/creative-courage-vidhyan-as-a-nation-we-have-lot-of-obligations-no-obligation-uaIAIw3ELuk69mhZ5I",
        title = "Joe Biden GIF by Creative Courage",
        type = "gif",
        username = "creative-courage",
        trendingDateTime = Date(-62170156725000),
        importDateTime = Date(1605041010000)
    )

    val mockDomainModel2 = GiphyImageItemDomainModel(
        id = "uaIAIw3ELuk69mhZ5I",
        previewUrl = "https://media3.giphy.com/media/uaIAIw3ELuk69mhZ5I/200w.gif",
        imageUrl = "https://media3.giphy.com/media/uaIAIw3ELuk69mhZ5I/giphy.gif",
        webUrl = "https://giphy.com/gifs/creative-courage-vidhyan-as-a-nation-we-have-lot-of-obligations-no-obligation-uaIAIw3ELuk69mhZ5I",
        title = "Joe Biden GIF by Creative Courage",
        type = "gif",
        username = "creative-courage"
    )

    val mockTrendingEntity3 = TrendingEntity(
        "etKSrsbbKbqwW6vzOg",
        previewUrl = "https://media1.giphy.com/media/etKSrsbbKbqwW6vzOg/200w.gif",
        imageUrl = "https://media1.giphy.com/media/etKSrsbbKbqwW6vzOg/giphy.gif",
        webUrl = "https://giphy.com/gifs/studiosoriginals-chicken-dinner-winner-etKSrsbbKbqwW6vzOg",
        title = "Winner Winner Win GIF by GIPHY Studios Originals",
        type = "gif",
        username = "studiosoriginals",
        trendingDateTime = Date(1636678809000),
        importDateTime = Date(1636417528000)
    )

    val mockDomainModel3 = GiphyImageItemDomainModel(
        "etKSrsbbKbqwW6vzOg",
        previewUrl = "https://media1.giphy.com/media/etKSrsbbKbqwW6vzOg/200w.gif",
        imageUrl = "https://media1.giphy.com/media/etKSrsbbKbqwW6vzOg/giphy.gif",
        webUrl = "https://giphy.com/gifs/studiosoriginals-chicken-dinner-winner-etKSrsbbKbqwW6vzOg",
        title = "Winner Winner Win GIF by GIPHY Studios Originals",
        type = "gif",
        username = "studiosoriginals"
    )
}
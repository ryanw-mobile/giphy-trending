package uk.ryanwong.giphytrending.data.source.local

import uk.ryanwong.giphytrending.data.source.network.model.FixedHeight
import uk.ryanwong.giphytrending.data.source.network.model.FixedWidth
import uk.ryanwong.giphytrending.data.source.network.model.Images
import uk.ryanwong.giphytrending.data.source.network.model.Original
import uk.ryanwong.giphytrending.data.source.network.model.TrendingData
import uk.ryanwong.giphytrending.domain.model.GiphyImageItemDomainModel
import java.util.Date

internal object TrendingMapperKtTestData {

    val mockTrendingData1 by lazy {
        TrendingData(
            analytics_response_payload = "some-analytics-response-payload",
            bitly_gif_url = "https://some.url/some-path",
            bitly_url = "https://some.url/some-path",
            content_url = "",
            embed_url = "https://some.url/some-path",
            id = "5KF7hci72bv2ZbchuT",
            images = Images(
                fixed_height = FixedHeight(
                    height = "200",
                    mp4 = "https://some.url/some-path",
                    mp4_size = "101674",
                    size = "500824",
                    url = "https://some.url/some-path",
                    webp = "https://some.url/some-path",
                    webp_size = "227744",
                    width = "265"
                ),
                fixed_width = FixedWidth(
                    height = "151",
                    mp4 = "https://some.url/some-path",
                    mp4_size = "65384",
                    size = "317527",
                    url = "https://media2.giphy.com/media/5KF7hci72bv2ZbchuT/200w.gif",
                    webp = "https://some.url/some-path",
                    webp_size = "153256",
                    width = "200"
                ),
                original = Original(
                    frames = "26",
                    hash = "some-hash",
                    height = "362",
                    mp4 = "https://some.url/some-path",
                    mp4_size = "273620",
                    size = "1749887",
                    url = "https://media2.giphy.com/media/5KF7hci72bv2ZbchuT/giphy.gif",
                    webp = "https://some.url/some-path",
                    webp_size = "455910",
                    width = "480"
                )
            ),
            import_datetime = Date(1635978358000),
            is_sticker = 0,
            rating = "g",
            slug = "moonagedaydream-neon-rated-moonage-daydream-UeaVRMdWRGzvzr62pF",
            source = "https://some.url/some-path",
            source_post_url = "https://some.url/some-path",
            source_tld = "neonrated.com",
            title = "Chef Cooking GIF by GIPHY Studios Originals",
            trending_datetime = Date(1636679711000),
            type = "gif",
            url = "https://giphy.com/gifs/studiosoriginals-cooking-chef-cook-5KF7hci72bv2ZbchuT",
            username = "studiosoriginals"
        )
    }

    val mockTrendingEntity1 by lazy {
        TrendingEntity(
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
    }

    val mockDomainModel1 by lazy {
        GiphyImageItemDomainModel(
            id = "5KF7hci72bv2ZbchuT",
            previewUrl = "https://media2.giphy.com/media/5KF7hci72bv2ZbchuT/200w.gif",
            imageUrl = "https://media2.giphy.com/media/5KF7hci72bv2ZbchuT/giphy.gif",
            webUrl = "https://giphy.com/gifs/studiosoriginals-cooking-chef-cook-5KF7hci72bv2ZbchuT",
            title = "Chef Cooking GIF by GIPHY Studios Originals",
            type = "gif",
            username = "studiosoriginals"
        )
    }

    val mockTrendingData2 by lazy {
        TrendingData(
            analytics_response_payload = "some-analytics-response-payload",
            bitly_gif_url = "https://some.url/some-path",
            bitly_url = "https://some.url/some-path",
            content_url = "",
            embed_url = "https://some.url/some-path",
            id = "uaIAIw3ELuk69mhZ5I",
            images = Images(
                fixed_height = FixedHeight(
                    height = "200",
                    mp4 = "https://some.url/some-path",
                    mp4_size = "101674",
                    size = "500824",
                    url = "https://some.url/some-path",
                    webp = "https://some.url/some-path",
                    webp_size = "227744",
                    width = "265"
                ),
                fixed_width = FixedWidth(
                    height = "151",
                    mp4 = "https://some.url/some-path",
                    mp4_size = "65384",
                    size = "317527",
                    url = "https://media3.giphy.com/media/uaIAIw3ELuk69mhZ5I/200w.gif",
                    webp = "https://some.url/some-path",
                    webp_size = "153256",
                    width = "200"
                ),
                original = Original(
                    frames = "26",
                    hash = "some-hash",
                    height = "362",
                    mp4 = "https://some.url/some-path",
                    mp4_size = "273620",
                    size = "1749887",
                    url = "https://media3.giphy.com/media/uaIAIw3ELuk69mhZ5I/giphy.gif",
                    webp = "https://some.url/some-path",
                    webp_size = "455910",
                    width = "480"
                )
            ),
            import_datetime = Date(1605041010000),
            is_sticker = 0,
            rating = "g",
            slug = "moonagedaydream-neon-rated-moonage-daydream-UeaVRMdWRGzvzr62pF",
            source = "https://neonrated.com/films/bowie-moonage-daydream",
            source_post_url = "https://neonrated.com/films/bowie-moonage-daydream",
            source_tld = "neonrated.com",
            title = "Joe Biden GIF by Creative Courage",
            trending_datetime = Date(-62170156725000),
            type = "gif",
            url = "https://giphy.com/gifs/creative-courage-vidhyan-as-a-nation-we-have-lot-of-obligations-no-obligation-uaIAIw3ELuk69mhZ5I",
            username = "creative-courage"
        )
    }

    val mockTrendingEntity2 by lazy {
        TrendingEntity(
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
    }

    val mockDomainModel2 by lazy {
        GiphyImageItemDomainModel(
            id = "uaIAIw3ELuk69mhZ5I",
            previewUrl = "https://media3.giphy.com/media/uaIAIw3ELuk69mhZ5I/200w.gif",
            imageUrl = "https://media3.giphy.com/media/uaIAIw3ELuk69mhZ5I/giphy.gif",
            webUrl = "https://giphy.com/gifs/creative-courage-vidhyan-as-a-nation-we-have-lot-of-obligations-no-obligation-uaIAIw3ELuk69mhZ5I",
            title = "Joe Biden GIF by Creative Courage",
            type = "gif",
            username = "creative-courage"
        )
    }

    val mockTrendingData3 by lazy {
        TrendingData(
            analytics_response_payload = "some-analytics-response-payload",
            bitly_gif_url = "https://some.url/some-path",
            bitly_url = "https://some.url/some-path",
            content_url = "",
            embed_url = "https://some.url/some-path",
            id = "etKSrsbbKbqwW6vzOg",
            images = Images(
                fixed_height = FixedHeight(
                    height = "200",
                    mp4 = "https://some.url/some-path",
                    mp4_size = "101674",
                    size = "500824",
                    url = "https://some.url/some-path",
                    webp = "https://some.url/some-path",
                    webp_size = "227744",
                    width = "265"
                ),
                fixed_width = FixedWidth(
                    height = "151",
                    mp4 = "https://some.url/some-path",
                    mp4_size = "65384",
                    size = "317527",
                    url = "https://media1.giphy.com/media/etKSrsbbKbqwW6vzOg/200w.gif",
                    webp = "https://some.url/some-path",
                    webp_size = "153256",
                    width = "200"
                ),
                original = Original(
                    frames = "26",
                    hash = "some-hash",
                    height = "362",
                    mp4 = "https://some.url/some-path",
                    mp4_size = "273620",
                    size = "1749887",
                    url = "https://media1.giphy.com/media/etKSrsbbKbqwW6vzOg/giphy.gif",
                    webp = "https://some.url/some-path",
                    webp_size = "455910",
                    width = "480"
                )
            ),
            import_datetime = Date(1636417528000),
            is_sticker = 0,
            rating = "g",
            slug = "moonagedaydream-neon-rated-moonage-daydream-UeaVRMdWRGzvzr62pF",
            source = "https://neonrated.com/films/bowie-moonage-daydream",
            source_post_url = "https://neonrated.com/films/bowie-moonage-daydream",
            source_tld = "neonrated.com",
            title = "Winner Winner Win GIF by GIPHY Studios Originals",
            trending_datetime = Date(1636678809000),
            type = "gif",
            url = "https://giphy.com/gifs/studiosoriginals-chicken-dinner-winner-etKSrsbbKbqwW6vzOg",
            username = "studiosoriginals"
        )
    }

    val mockTrendingEntity3 by lazy {
        TrendingEntity(
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
    }

    val mockDomainModel3 by lazy {
        GiphyImageItemDomainModel(
            "etKSrsbbKbqwW6vzOg",
            previewUrl = "https://media1.giphy.com/media/etKSrsbbKbqwW6vzOg/200w.gif",
            imageUrl = "https://media1.giphy.com/media/etKSrsbbKbqwW6vzOg/giphy.gif",
            webUrl = "https://giphy.com/gifs/studiosoriginals-chicken-dinner-winner-etKSrsbbKbqwW6vzOg",
            title = "Winner Winner Win GIF by GIPHY Studios Originals",
            type = "gif",
            username = "studiosoriginals"
        )
    }
}
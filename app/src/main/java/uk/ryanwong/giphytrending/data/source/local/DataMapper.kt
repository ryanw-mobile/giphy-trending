package uk.ryanwong.giphytrending.data.source.local

import uk.ryanwong.giphytrending.model.Data
import uk.ryanwong.giphytrending.model.FixedHeightSmallStill
import uk.ryanwong.giphytrending.model.Images

fun DataEntity.toData() = Data(
    Images(FixedHeightSmallStill(url = this.images, height = "320", width = "420")),
    this.title,
    this.type,
    this.username
)

fun List<DataEntity>.toDataList() = this.map { it.toData() }

fun Data.toDataEntity() = DataEntity(
    images = this.images.fixedHeightSmallStill.url,
    title = this.title,
    type = this.type,
    username = this.username
)

fun List<Data>.toDataEntityList() = this.map { it.toDataEntity() }
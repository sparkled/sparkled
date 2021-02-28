package io.sparkled.model.entity.v2

import io.sparkled.model.annotation.Entity
import io.sparkled.model.util.IdUtils

@Entity(name = "playlist", idField = "id")
data class PlaylistEntity(
    val id: Int = IdUtils.NO_ID,
    val name: String,
) : SparkledEntity

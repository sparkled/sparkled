package io.sparkled.model.entity.v2

import io.sparkled.model.annotation.Entity
import io.sparkled.model.util.IdUtils

@Entity(name = "song", idField = "id")
data class SongEntity(
    val id: Int = IdUtils.NO_ID,
    val name: String,
    val artist: String? = null,
    val durationMs: Int
) : SparkledEntity

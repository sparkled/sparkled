package io.sparkled.viewmodel

import io.sparkled.model.entity.v2.SongEntity
import io.sparkled.model.util.IdUtils

data class SongViewModel(
    val id: Int = IdUtils.NO_ID,
    val name: String,
    val artist: String?,
    val durationMs: Int,
) {
    fun toModel() = SongEntity(
        id = id,
        name = name,
        artist = artist,
        durationMs = durationMs,
    )

    companion object {
        fun fromModel(model: SongEntity) = SongViewModel(
            id = model.id,
            name = model.name,
            artist = model.artist,
            durationMs = model.durationMs,
        )
    }
}

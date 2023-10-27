package io.sparkled.viewmodel

import io.sparkled.model.SongModel
import io.sparkled.model.UniqueId

data class SongViewModel(
    val id: UniqueId,
    val name: String,
    val artist: String?,
    val durationMs: Int,
) : ViewModel {
    fun toModel() = SongModel(
        id = id,
        name = name,
        artist = artist,
        durationMs = durationMs,
    )

    companion object {
        fun fromModel(model: SongModel) = SongViewModel(
            id = model.id,
            name = model.name,
            artist = model.artist,
            durationMs = model.durationMs,
        )
    }
}

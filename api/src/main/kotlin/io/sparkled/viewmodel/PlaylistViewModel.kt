package io.sparkled.viewmodel

import io.sparkled.model.entity.v2.PlaylistEntity
import io.sparkled.model.entity.v2.PlaylistSequenceEntity
import io.sparkled.model.util.IdUtils

data class PlaylistViewModel(
    val id: Int = IdUtils.NO_ID,
    val name: String,
    val sequences: List<PlaylistSequenceViewModel> = emptyList()
) {
    fun toModel(): Pair<PlaylistEntity, List<PlaylistSequenceEntity>> {
        val playlist = PlaylistEntity(
            id = id,
            name = name
        )

        val playlistSequences = sequences.map { it.toModel(playlistId = id) }
        return playlist to playlistSequences
    }

    companion object {
        fun fromModel(model: PlaylistEntity, sequences: List<PlaylistSequenceEntity>) = PlaylistViewModel(
            id = model.id,
            name = model.name,
            sequences = sequences.map(PlaylistSequenceViewModel::fromModel)
        )
    }
}

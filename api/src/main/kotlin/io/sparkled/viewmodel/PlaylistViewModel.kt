package io.sparkled.viewmodel

import io.sparkled.model.PlaylistModel
import io.sparkled.model.PlaylistSequenceModel
import io.sparkled.model.UniqueId
import io.sparkled.model.annotation.GenerateClientType

@GenerateClientType
data class PlaylistViewModel(
    val id: UniqueId,
    val name: String,
    val sequences: List<PlaylistSequenceViewModel> = emptyList()
) : ViewModel {
    fun toModel(): Pair<PlaylistModel, List<PlaylistSequenceModel>> {
        val playlist = PlaylistModel(
            id = id,
            name = name
        )

        val playlistSequences = sequences.map { it.toModel(playlistId = id) }
        return playlist to playlistSequences
    }

    companion object {
        fun fromModel(model: PlaylistModel, sequences: List<PlaylistSequenceModel>) = PlaylistViewModel(
            id = model.id,
            name = model.name,
            sequences = sequences.map(PlaylistSequenceViewModel::fromModel)
        )
    }
}

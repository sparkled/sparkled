package io.sparkled.viewmodel

import io.sparkled.model.PlaylistModel
import io.sparkled.model.PlaylistSequenceModel
import io.sparkled.model.SequenceModel
import io.sparkled.model.SongModel
import io.sparkled.model.annotation.GenerateClientType
import io.sparkled.model.constant.ModelConstants

@GenerateClientType
data class PlaylistSummaryViewModel(
    val id: String,
    val name: String,
    val sequenceCount: Int,
    val durationMs: Int,
) : ViewModel {
    companion object {
        fun fromModel(
            model: PlaylistModel,
            playlistSequences: List<PlaylistSequenceModel>,
            sequences: List<SequenceModel>,
            songs: List<SongModel>
        ) = PlaylistSummaryViewModel(
            id = model.id,
            name = model.name,
            sequenceCount = playlistSequences.size,
            durationMs = playlistSequences.sumOf { ps ->
                val sequence = sequences.first { it.id == ps.sequenceId }
                val song = songs.first { it.id == sequence.songId }
                song.durationMs
            }
        )
    }
}

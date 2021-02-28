package io.sparkled.viewmodel

import io.sparkled.model.constant.ModelConstants
import io.sparkled.model.entity.v2.PlaylistEntity
import io.sparkled.model.entity.v2.PlaylistSequenceEntity
import io.sparkled.model.entity.v2.SequenceEntity
import io.sparkled.model.entity.v2.SongEntity

data class PlaylistSummaryViewModel(
    val id: Int,
    val name: String,
    val sequenceCount: Int,
    val durationSeconds: Int,
) {
    companion object {
        fun fromModel(
            model: PlaylistEntity,
            playlistSequences: List<PlaylistSequenceEntity>,
            sequences: List<SequenceEntity>,
            songs: List<SongEntity>
        ) = PlaylistSummaryViewModel(
            id = model.id,
            name = model.name,
            sequenceCount = playlistSequences.size,
            durationSeconds = playlistSequences.sumOf { ps ->
                val sequence = sequences.first { it.id == ps.sequenceId }
                val song = songs.first { it.id == sequence.songId }
                song.durationMs
            } / ModelConstants.MS_PER_SECOND
        )
    }
}

package io.sparkled.viewmodel

import io.sparkled.model.entity.v2.PlaylistSequenceEntity
import io.sparkled.model.util.IdUtils
import java.util.*

data class PlaylistSequenceViewModel(
    val uuid: UUID = IdUtils.newUuid(),
    val sequenceId: Int,
    val displayOrder: Int,
) {
    fun toModel(playlistId: Int) = PlaylistSequenceEntity(
        uuid = uuid,
        playlistId = playlistId,
        sequenceId = sequenceId,
        displayOrder = displayOrder
    )

    companion object {
        fun fromModel(model: PlaylistSequenceEntity) = PlaylistSequenceViewModel(
            uuid = model.uuid,
            sequenceId = model.sequenceId,
            displayOrder = model.displayOrder,
        )
    }
}

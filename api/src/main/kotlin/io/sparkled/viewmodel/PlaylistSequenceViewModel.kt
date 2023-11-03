package io.sparkled.viewmodel

import io.sparkled.model.PlaylistSequenceModel
import io.sparkled.model.UniqueId

data class PlaylistSequenceViewModel(
    val id: UniqueId,
    val sequenceId: UniqueId,
    val displayOrder: Int,
) : ViewModel {
    fun toModel(playlistId: UniqueId) = PlaylistSequenceModel(
        id = id,
        playlistId = playlistId,
        sequenceId = sequenceId,
        displayOrder = displayOrder
    )

    companion object {
        fun fromModel(model: PlaylistSequenceModel) = PlaylistSequenceViewModel(
            id = model.id,
            sequenceId = model.sequenceId,
            displayOrder = model.displayOrder,
        )
    }
}

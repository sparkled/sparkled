package io.sparkled.viewmodel

import io.sparkled.model.PlaylistSequenceModel
import io.sparkled.model.UniqueId
import io.sparkled.model.annotation.GenerateClientType

@GenerateClientType
data class PlaylistSequenceInsertionViewModel(
    val sequenceId: UniqueId,
    val displayOrder: Int,
) : ViewModel {
    fun toModel(playlistId: UniqueId) = PlaylistSequenceModel(
        playlistId = playlistId,
        sequenceId = sequenceId,
        displayOrder = displayOrder,
    )
}

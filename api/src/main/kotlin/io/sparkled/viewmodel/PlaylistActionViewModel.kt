package io.sparkled.viewmodel

import io.sparkled.model.UniqueId
import io.sparkled.model.annotation.GenerateClientType

@GenerateClientType
data class PlaylistActionViewModel(
    val action: PlaylistActionType,
    val playlistId: UniqueId? = null,
    val sequenceId: UniqueId? = null,
    val repeat: Boolean? = true,
)

package io.sparkled.model.playlist

import io.sparkled.model.UniqueId

data class PlaylistAction(
    val action: PlaylistActionType,
    val playlistId: UniqueId? = null,
    val sequenceId: UniqueId? = null,
    val repeat: Boolean? = true,
)

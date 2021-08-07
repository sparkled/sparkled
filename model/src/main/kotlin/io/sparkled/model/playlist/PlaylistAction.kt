package io.sparkled.model.playlist

data class PlaylistAction(
    val action: PlaylistActionType,
    val playlistId: Int? = null,
    val sequenceId: Int? = null,
    val repeat: Boolean? = true
)

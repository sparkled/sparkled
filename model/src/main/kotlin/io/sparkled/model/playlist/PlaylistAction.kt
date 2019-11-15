package io.sparkled.model.playlist

data class PlaylistAction(
    val type: PlaylistActionType? = null,
    val playlistId: Int? = null,
    val sequenceId: Int? = null
)

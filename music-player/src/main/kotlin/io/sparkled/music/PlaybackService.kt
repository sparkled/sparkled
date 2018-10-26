package io.sparkled.music

import io.sparkled.model.entity.Playlist

/**
 * Plays and stops playlists.
 */
interface PlaybackService {

    /**
     * Begins playback of the provided playlist, on repeat.

     * @param playlist The playlist to be played.
     */
    fun play(playlist: Playlist)

    /**
     * Stops playback of the current playlist. If no playlist is playing, this is a no-op.
     */
    fun stopPlayback()
}

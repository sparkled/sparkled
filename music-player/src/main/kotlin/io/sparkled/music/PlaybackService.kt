package io.sparkled.music

import io.sparkled.model.entity.v2.SequenceEntity

/**
 * Plays and stops playlists.
 */
interface PlaybackService {

    /**
     * Begins playback of the provided sequences, on repeat.

     * @param sequences The sequences to be played
     */
    fun play(sequences: List<SequenceEntity>, repeat: Boolean)

    /**
     * Stops playback of the current playlist. If no playlist is playing, this is a no-op.
     */
    fun stopPlayback()
}

package io.sparkled.music

/**
 * Plays and stops playlists.
 */
interface PlaybackService {

    /**
     * Begins playback of the provided sequences, on repeat.

     * @param sequences The sequences to be played
     */
    fun play(sequences: List<SequenceModel>, repeat: Boolean)

    /**
     * Stops playback of the current playlist. If no playlist is playing, this is a no-op.
     */
    fun stopPlayback()
}

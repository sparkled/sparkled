package io.sparkled.music

import javax.sound.sampled.LineListener

/**
 * Plays [SongAudio] data and provides a means of determining song progress.
 */
interface MusicPlayerService {

    /**
     * Attaches a listener that will be notified when a sequence is finished playing or is stopped by a call to
     * [.stopPlayback].

     * @param listener The listener to attach
     */
    fun addLineListener(listener: LineListener)

    /**
     * Plays the provided audio data. Calls to this method will block until playback has completed. Therefore, it is the
     * requirement of the caller to run this method in a new thread.

     * @param playbackState A playback state containing the [SongAudio] data to be played.
     */
    fun play(playbackState: SequencePlaybackState)

    /**
     * Plays an audio clip without keeping track of any state.
     */
    fun playOneShot(songAudio: ByteArray)

    /**
     * @return A normalised value between 0 and 1 indicating the playback progress of the current sequence.
     */
    val sequenceProgress: Double

    /**
     * Stops playback of the current sequence. If no sequence is playing, this is a no-op.
     */
    fun stopPlayback()
}

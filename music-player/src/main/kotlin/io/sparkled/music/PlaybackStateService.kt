package io.sparkled.music

/**
 * Provides read-only access to the current playback state.
 */
interface PlaybackStateService {
    val state: PlaybackState
}

package io.sparkled.music;

/**
 * Provides read-only access to the current playback state.
 */
public interface PlaybackStateService {

    /**
     * @return Information pertaining to the sequence that is currently playing, or an empty object if no sequence is
     * playing.
     * @see PlaybackState#isEmpty()
     */
    PlaybackState getPlaybackState();
}

package io.sparkled.music;

import io.sparkled.model.entity.Playlist;

/**
 * Plays and stops playlists.
 */
public interface PlaybackService {

    /**
     * Begins playback of the provided playlist, on repeat.
     *
     * @param playlist The playlist to be played.
     */
    void play(Playlist playlist);

    /**
     * Stops playback of the current playlist. If no playlist is playing, this is a no-op.
     */
    void stopPlayback();
}

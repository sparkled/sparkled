package net.chrisparton.sparkled.music;

import javazoom.jl.player.advanced.PlaybackListener;
import net.chrisparton.sparkled.entity.Song;
import net.chrisparton.sparkled.entity.SongData;
import net.chrisparton.sparkled.renderdata.RenderedChannelMap;

public interface SongPlayerService {

    /**
     * Plays the provided song and provides the associated song data for synchronised retrieval.
     */
    void play(Song song, SongData songData);

    /**
     * @return The song that is currently playing, or null if no song is playing.
     */
    Song getCurrentSong();

    /**
     * @return The rendered data for the song that is currently playing, or null if no song is playing.
     */
    RenderedChannelMap getRenderedChannelMap();

    /**
     * @return A value between 0 and 1 indicating the playback progress of the current song.
     */
    double getSongProgress();

    /**
     * @param playbackListener The playback listener to register.
     */
    void setPlaybackListener(PlaybackListener playbackListener);
}
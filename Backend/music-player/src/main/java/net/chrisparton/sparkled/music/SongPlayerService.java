package net.chrisparton.sparkled.music;

import javazoom.jl.player.advanced.PlaybackEvent;
import net.chrisparton.sparkled.model.entity.Song;
import net.chrisparton.sparkled.model.entity.SongAudio;
import net.chrisparton.sparkled.model.render.RenderedChannelMap;

import java.util.function.Consumer;

public interface SongPlayerService {

    /**
     * Plays the provided song and provides the associated song data for synchronised retrieval.
     */
    void play(Song song, SongAudio songAudio);

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
    void addPlaybackFinishedListener(Consumer<PlaybackEvent> playbackListener);
}

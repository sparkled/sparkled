package io.sparkled.music;

import javazoom.jl.player.advanced.PlaybackEvent;
import io.sparkled.model.entity.Song;
import io.sparkled.model.entity.SongAudio;
import io.sparkled.model.render.RenderedStagePropDataMap;

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
    RenderedStagePropDataMap getRenderedStagePropDataMap();

    /**
     * @return A value between 0 and 1 indicating the playback progress of the current song.
     */
    double getSongProgress();

    /**
     * @param playbackListener The playback listener to register.
     */
    void addPlaybackFinishedListener(Consumer<PlaybackEvent> playbackListener);

    /**
     * Stops playback of the current scheduled song. If no song is playing, this is a NOOP.
     */
    void stopCurrentSong();
}

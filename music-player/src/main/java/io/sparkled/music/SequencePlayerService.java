package io.sparkled.music;

import io.sparkled.model.entity.Sequence;
import javazoom.jl.player.advanced.PlaybackEvent;
import io.sparkled.model.entity.SongAudio;
import io.sparkled.model.render.RenderedStagePropDataMap;

import java.util.function.Consumer;

public interface SequencePlayerService {

    /**
     * Plays the provided sequence and provides the associated sequence data for synchronised retrieval.
     */
    void play(Sequence sequence, SongAudio songAudio);

    /**
     * @return The sequence that is currently playing, or null if no sequence is playing.
     */
    Sequence getCurrentSequence();

    /**
     * @return The rendered data for the sequence that is currently playing, or null if no sequence is playing.
     */
    RenderedStagePropDataMap getRenderedStagePropDataMap();

    /**
     * @return A value between 0 and 1 indicating the playback progress of the current sequence.
     */
    double getSequenceProgress();

    /**
     * @param playbackListener The playback listener to register.
     */
    void addPlaybackFinishedListener(Consumer<PlaybackEvent> playbackListener);

    /**
     * Stops playback of the current scheduled sequence. If no sequence is playing, this is a NOOP.
     */
    void stopCurrentSequence();
}

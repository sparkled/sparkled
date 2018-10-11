package io.sparkled.music;

import io.sparkled.model.entity.Playlist;
import io.sparkled.model.entity.Sequence;
import io.sparkled.model.render.RenderedStagePropDataMap;
import javazoom.jl.player.advanced.PlaybackEvent;

import java.util.UUID;
import java.util.function.Consumer;

public interface PlaylistService {

    /**
     * Plays the provided playlist and provides the associated sequence data for synchronised retrieval.
     */
    void play(Playlist playlist);

    /**
     * @return The sequence that is currently playing, or null if no sequence is playing.
     */
    Sequence getCurrentSequence();

    /**
     * @return The rendered data for the sequence that is currently playing, or null if no sequence is playing.
     */
    RenderedStagePropDataMap getRenderedStageProps();

    /**
     * @param stagePropCode The code of the stage prop being searched for.
     * @return The stage prop UUID.
     */
    UUID getStagePropUuid(String stagePropCode);

    /**
     * @return A value between 0 and 1 indicating the playback progress of the current sequence.
     */
    double getSequenceProgress();

    /**
     * Stops playback of the current sequence. If no sequence is playing, this is a no-op.
     */
    void stopPlayback();
}

package io.sparkled.music;

import io.sparkled.model.entity.SongAudio;
import javazoom.jl.player.advanced.PlaybackEvent;

import java.util.function.Consumer;

/**
 * Plays {@link SongAudio} data and provides a means of determining song progress.
 */
public interface MusicPlayerService {

    /**
     * Attaches a listener that will be notified when a sequence is finished playing or is stopped by a call to
     * {@link #stopPlayback()}.
     *
     * @param listener The listener to attach
     */
    void addSequenceFinishListener(Consumer<PlaybackEvent> listener);

    /**
     * Plays the provided audio data. Calls to this method will block until playback has completed. Therefore, it is the
     * requirement of the caller to run this method in a new thread.
     *
     * @param playbackState A playback state containing the {@link SongAudio} data to be played.
     */
    void play(PlaybackState playbackState);

    /**
     * @return A normalised value between 0 and 1 indicating the playback progress of the current sequence.
     */
    double getSequenceProgress(PlaybackState playbackState);

    /**
     * Stops playback of the current sequence. If no sequence is playing, this is a no-op.
     */
    void stopPlayback();
}

package io.sparkled.music;

import javazoom.jl.player.advanced.PlaybackEvent;
import javazoom.jl.player.advanced.PlaybackListener;

import java.util.HashSet;
import java.util.Set;
import java.util.function.Consumer;

public class AggregatePlaybackListener extends PlaybackListener {

    private final Set<Consumer<PlaybackEvent>> playbackStartedListeners = new HashSet<>();
    private final Set<Consumer<PlaybackEvent>> playbackFinishedListeners = new HashSet<>();

    void addPlaybackFinishedListener(Consumer<PlaybackEvent> listener) {
        playbackFinishedListeners.add(listener);
    }

    @Override
    public void playbackStarted(PlaybackEvent event) {
        playbackStartedListeners.forEach(l -> l.accept(event));
    }

    @Override
    public void playbackFinished(PlaybackEvent event) {
        playbackFinishedListeners.forEach(l -> l.accept(event));
    }
}

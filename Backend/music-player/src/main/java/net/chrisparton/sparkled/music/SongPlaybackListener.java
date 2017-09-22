package net.chrisparton.sparkled.music;

import javazoom.jl.player.advanced.PlaybackEvent;
import javazoom.jl.player.advanced.PlaybackListener;

public class SongPlaybackListener extends PlaybackListener {

    private Runnable onFinish;

    public SongPlaybackListener(Runnable onFinish) {
        this.onFinish = onFinish;
    }

    @Override
    public void playbackFinished(PlaybackEvent evt) {
        onFinish.run();
    }
}
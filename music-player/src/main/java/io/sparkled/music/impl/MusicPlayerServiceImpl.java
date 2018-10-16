package io.sparkled.music.impl;

import io.sparkled.model.entity.SongAudio;
import io.sparkled.music.MusicPlayerService;
import io.sparkled.music.PlaybackState;
import javazoom.jl.player.AudioDevice;
import javazoom.jl.player.FactoryRegistry;
import javazoom.jl.player.advanced.AdvancedPlayer;
import javazoom.jl.player.advanced.PlaybackEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import java.io.ByteArrayInputStream;
import java.util.function.Consumer;

public class MusicPlayerServiceImpl implements MusicPlayerService {

    private static final Logger logger = LoggerFactory.getLogger(MusicPlayerServiceImpl.class);

    private final AggregatePlaybackListener playbackListener = new AggregatePlaybackListener();
    private AudioDevice audioDevice;

    @Inject
    public MusicPlayerServiceImpl() {
    }

    @Override
    public void play(PlaybackState playbackState) {
        try {
            logger.error("Playing sequence {}.", playbackState.getSequence().getName());
            FactoryRegistry r = FactoryRegistry.systemRegistry();
            audioDevice = r.createAudioDevice();

            ByteArrayInputStream inputStream = new ByteArrayInputStream(playbackState.getSongAudio().getAudioData());
            AdvancedPlayer player = new AdvancedPlayer(inputStream, audioDevice);
            player.setPlayBackListener(playbackListener);
            player.play();
        } catch (Exception e) {
            logger.error("Failed to play sequence {}: {}.", playbackState.getSequence().getName(), e.getMessage());
        }
    }

    @Override
    public void addSequenceFinishListener(Consumer<PlaybackEvent> listener) {
        this.playbackListener.addPlaybackFinishedListener(listener);
        logger.info("Added playback listener: {}.", listener);
    }

    @Override
    public double getSequenceProgress(PlaybackState playbackState) {
        if (audioDevice == null || playbackState.isEmpty()) {
            return 0d;
        } else {
            int playbackMs = audioDevice.getPosition();
            double durationMs = playbackState.getSong().getDurationMs();
            return Math.min(1f, playbackMs / durationMs);
        }
    }

    @Override
    public void stopPlayback() {
        if (audioDevice != null && audioDevice.isOpen()) {
            audioDevice.close();
            logger.info("Audio device closed.");
        } else {
            logger.info("Audio device is already closed.");
        }

        audioDevice = null;
    }
}

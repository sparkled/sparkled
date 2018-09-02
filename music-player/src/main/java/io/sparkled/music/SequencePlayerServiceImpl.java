package io.sparkled.music;

import io.sparkled.model.entity.Sequence;
import io.sparkled.model.entity.SongAudio;
import io.sparkled.model.render.RenderedStagePropDataMap;
import io.sparkled.persistence.sequence.SequencePersistenceService;
import javazoom.jl.decoder.JavaLayerException;
import javazoom.jl.player.AudioDevice;
import javazoom.jl.player.FactoryRegistry;
import javazoom.jl.player.advanced.AdvancedPlayer;
import javazoom.jl.player.advanced.PlaybackEvent;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.io.ByteArrayInputStream;
import java.util.function.Consumer;
import java.util.logging.Level;
import java.util.logging.Logger;

@Singleton
public class SequencePlayerServiceImpl implements SequencePlayerService {

    private final Logger logger = Logger.getLogger(SequencePlayerServiceImpl.class.getName());
    private final SequencePersistenceService sequencePersistenceService;
    private final AggregatePlaybackListener playbackListener = new AggregatePlaybackListener();
    private AudioDevice audioDevice;
    private Sequence currentSequence;
    private RenderedStagePropDataMap renderedStagePropDataMap;

    @Inject
    public SequencePlayerServiceImpl(SequencePersistenceService sequencePersistenceService) {
        this.sequencePersistenceService = sequencePersistenceService;
        this.playbackListener.addPlaybackFinishedListener(event -> {
            logger.info("Sequence playback finished.");
            this.stopCurrentSequence();
        });
    }

    @Override
    public void play(Sequence sequence, SongAudio songAudio) {
        logger.info("Playing sequence " + sequence.getName());
        this.currentSequence = sequence;
        this.renderedStagePropDataMap = sequencePersistenceService.getRenderedStageProps(sequence);

        try {
            FactoryRegistry r = FactoryRegistry.systemRegistry();
            audioDevice = r.createAudioDevice();

            ByteArrayInputStream inputStream = new ByteArrayInputStream(songAudio.getAudioData());
            AdvancedPlayer player = new AdvancedPlayer(inputStream, audioDevice);
            player.setPlayBackListener(playbackListener);
            player.play();
        } catch (JavaLayerException e) {
            logger.log(Level.SEVERE, "Failed to play sequence " + sequence.getId() + ": " + e.getMessage());
        }
    }

    @Override
    public Sequence getCurrentSequence() {
        return currentSequence;
    }

    @Override
    public RenderedStagePropDataMap getRenderedStagePropDataMap() {
        return renderedStagePropDataMap;
    }

    @Override
    public double getSequenceProgress() {
        if (audioDevice == null || currentSequence == null) {
            return 0d;
        } else {
            int playbackMillis = audioDevice.getPosition();
            double durationMillis = currentSequence.getDurationFrames() / (double) currentSequence.getFramesPerSecond() * 1000;
            return Math.min(1f, playbackMillis / durationMillis);
        }
    }

    @Override
    public void addPlaybackFinishedListener(Consumer<PlaybackEvent> playbackListener) {
        this.playbackListener.addPlaybackFinishedListener(playbackListener);
    }

    @Override
    public void stopCurrentSequence() {
        if (this.audioDevice != null && this.audioDevice.isOpen()) {
            this.audioDevice.close();
        }

        this.currentSequence = null;
        this.renderedStagePropDataMap = null;
        this.audioDevice = null;
    }
}

package io.sparkled.music;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import io.sparkled.model.entity.Playlist;
import io.sparkled.model.entity.Sequence;
import io.sparkled.model.entity.Song;
import io.sparkled.model.entity.SongAudio;
import io.sparkled.model.render.RenderedStagePropDataMap;
import io.sparkled.persistence.playlist.PlaylistPersistenceService;
import io.sparkled.persistence.sequence.SequencePersistenceService;
import io.sparkled.persistence.song.SongPersistenceService;
import javazoom.jl.decoder.JavaLayerException;
import javazoom.jl.player.AudioDevice;
import javazoom.jl.player.FactoryRegistry;
import javazoom.jl.player.advanced.AdvancedPlayer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.io.ByteArrayInputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

@Singleton
public class PlaylistServiceImpl implements PlaylistService {

    private static final Logger logger = LoggerFactory.getLogger(PlaylistServiceImpl.class);

    private final SongPersistenceService songPersistenceService;
    private final SequencePersistenceService sequencePersistenceService;
    private final PlaylistPersistenceService playlistPersistenceService;
    private final AggregatePlaybackListener playbackListener = new AggregatePlaybackListener();
    private final ExecutorService executor;
    private AudioDevice audioDevice;
    private Song currentSong;
    private Sequence currentSequence;
    private RenderedStagePropDataMap renderedStageProps;
    private Map<String, UUID> stagePropUuidMap = new HashMap<>();
    private Playlist playlist;
    private AtomicInteger playlistIndex = new AtomicInteger(0);

    @Inject
    public PlaylistServiceImpl(SongPersistenceService songPersistenceService,
                               SequencePersistenceService sequencePersistenceService,
                               PlaylistPersistenceService playlistPersistenceService) {
        this.songPersistenceService = songPersistenceService;
        this.sequencePersistenceService = sequencePersistenceService;
        this.playlistPersistenceService = playlistPersistenceService;

        this.executor = Executors.newSingleThreadScheduledExecutor(
                new ThreadFactoryBuilder().setNameFormat("playlist-service-%d").build()
        );

        this.playbackListener.addPlaybackFinishedListener(event -> {
            if (playlist != null) {
                this.playlistIndex.incrementAndGet();
                playNextSequence();
            }
        });
    }

    @Override
    public void play(Playlist playlist) {
        logger.info("Playing playlist {}.", playlist.getName());
        this.playlist = playlist;
        beginPlaylist();
    }

    private void beginPlaylist() {
        this.playlistIndex.set(0);
        executor.submit(this::playNextSequence);
    }

    private void playNextSequence() {
        int index = this.playlistIndex.get();
        Optional<Sequence> sequenceOptional = playlistPersistenceService.getSequenceAtPlaylistIndex(playlist.getId(), index);

        if (!sequenceOptional.isPresent()) {
            if (index > 0) {
                logger.info("Finished playlist {}, restarting.", playlist.getId());
                beginPlaylist();
            } else {
                logger.error("Failed to play playlist {}: playlist is empty.", playlist.getId());
            }
        } else {
            try {
                currentSequence = sequenceOptional.get();
                currentSong = songPersistenceService.getSongBySequenceId(currentSequence.getId()).orElse(null);
                if (currentSong == null) {
                    logger.error("Failed to play sequence {}: Song not found.", currentSequence.getId());
                } else {
                    playSequence(currentSequence);
                }
            } catch (Exception e) {
                logger.error("Failed to play sequence {}: {}.", currentSequence.getId(), e.getMessage());
            }
        }
    }

    private void playSequence(Sequence sequence) throws JavaLayerException {
        logger.info("Playing sequence {}.", sequence.getId());

        renderedStageProps = sequencePersistenceService.getRenderedStagePropsBySequenceAndSong(currentSequence, currentSong);
        stagePropUuidMap = sequencePersistenceService.getSequenceStagePropUuidMapBySequenceId(currentSequence.getId());
        Optional<SongAudio> audio = sequencePersistenceService.getSongAudioBySequenceId(currentSequence.getId());

        FactoryRegistry r = FactoryRegistry.systemRegistry();
        audioDevice = r.createAudioDevice();

        if (audio.isPresent()) {
            ByteArrayInputStream inputStream = new ByteArrayInputStream(audio.get().getAudioData());
            AdvancedPlayer player = new AdvancedPlayer(inputStream, audioDevice);
            player.setPlayBackListener(playbackListener);
            player.play();
        } else {
            logger.error("Failed to play sequence {}: sequence audio does not exist.", currentSequence.getId());
        }
    }

    @Override
    public Song getCurrentSong() {
        return currentSong;
    }

    @Override
    public Sequence getCurrentSequence() {
        return currentSequence;
    }

    @Override
    public RenderedStagePropDataMap getRenderedStageProps() {
        return renderedStageProps;
    }

    @Override
    public UUID getStagePropUuid(String stagePropCode) {
        return stagePropUuidMap.get(stagePropCode);
    }

    @Override
    public double getSequenceProgress() {
        if (audioDevice == null || currentSequence == null) {
            return 0d;
        } else {
            int playbackMs = audioDevice.getPosition();
            double durationMs = currentSong.getDurationMs();
            return Math.min(1f, playbackMs / durationMs);
        }
    }

    @Override
    public void stopPlayback() {
        if (audioDevice != null && audioDevice.isOpen()) {
            audioDevice.close();
        }

        playlistIndex.set(0);
        playlist = null;
        currentSequence = null;
        renderedStageProps = null;
        audioDevice = null;
    }
}

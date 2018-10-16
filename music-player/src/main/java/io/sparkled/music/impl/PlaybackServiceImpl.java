package io.sparkled.music.impl;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import com.google.inject.persist.UnitOfWork;
import io.sparkled.model.entity.Playlist;
import io.sparkled.model.entity.Sequence;
import io.sparkled.model.entity.Song;
import io.sparkled.model.entity.SongAudio;
import io.sparkled.model.render.RenderedStagePropDataMap;
import io.sparkled.music.MusicPlayerService;
import io.sparkled.music.PlaybackService;
import io.sparkled.music.PlaybackState;
import io.sparkled.music.PlaybackStateService;
import io.sparkled.persistence.playlist.PlaylistPersistenceService;
import io.sparkled.persistence.sequence.SequencePersistenceService;
import io.sparkled.persistence.song.SongPersistenceService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.sound.sampled.LineEvent;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicReference;

public class PlaybackServiceImpl implements PlaybackService, PlaybackStateService {

    private static final Logger logger = LoggerFactory.getLogger(PlaybackServiceImpl.class);
    private final ExecutorService executor;

    private AtomicReference<PlaybackState> playbackState = new AtomicReference<>(new PlaybackState());
    private final SongPersistenceService songPersistenceService;
    private final SequencePersistenceService sequencePersistenceService;
    private final PlaylistPersistenceService playlistPersistenceService;
    private final UnitOfWork unitOfWork;
    private final MusicPlayerService musicPlayerService;

    @Inject
    public PlaybackServiceImpl(SongPersistenceService songPersistenceService,
                               SequencePersistenceService sequencePersistenceService,
                               PlaylistPersistenceService playlistPersistenceService,
                               UnitOfWork unitOfWork,
                               MusicPlayerService musicPlayerService) {
        this.songPersistenceService = songPersistenceService;
        this.sequencePersistenceService = sequencePersistenceService;
        this.playlistPersistenceService = playlistPersistenceService;
        this.unitOfWork = unitOfWork;
        this.musicPlayerService = musicPlayerService;

        this.executor = Executors.newSingleThreadScheduledExecutor(
                new ThreadFactoryBuilder().setNameFormat("playback-service-%d").build()
        );

        musicPlayerService.addLineListener(this::onLineEvent);
    }

    private void onLineEvent(LineEvent event) {
        if (event.getType() == LineEvent.Type.STOP) {
            PlaybackState state = this.playbackState.get();
            if (!state.isEmpty()) {
                Playlist playlist = state.getPlaylist();
                int playlistIndex = state.getPlaylistIndex();
                submitSequencePlayback(playlist, playlistIndex + 1);
            }
        }
    }

    @Override
    public PlaybackState getPlaybackState() {
        return playbackState.get();
    }

    @Override
    public void play(Playlist playlist) {
        stopPlayback();

        logger.info("Playing playlist {}.", playlist.getName());
        submitSequencePlayback(playlist, 0);
    }

    private void submitSequencePlayback(final Playlist playlist, final int playlistIndex) {
        executor.submit(() -> playSequenceAtIndex(playlist, playlistIndex));
    }

    private void playSequenceAtIndex(Playlist playlist, int playlistIndex) {
        PlaybackState playbackState = loadPlaybackState(playlist, playlistIndex);
        this.playbackState.set(playbackState);

        if (!playbackState.isEmpty()) {
            musicPlayerService.play(playbackState);
        } else if (playlistIndex > 0) {
            logger.info("Finished playlist {}, restarting.", playlist.getId());
            playSequenceAtIndex(playlist, 0);
        } else {
            logger.error("Failed to play playlist {}: playlist is empty.", playlist.getId());
        }
    }

    private PlaybackState loadPlaybackState(Playlist playlist, int playlistIndex) {
        try {
            unitOfWork.begin();
            Sequence sequence = playlistPersistenceService.getSequenceAtPlaylistIndex(playlist.getId(), playlistIndex).orElse(null);

            if (sequence == null) {
                return new PlaybackState();
            } else {
                Song song = songPersistenceService.getSongBySequenceId(sequence.getId()).orElse(null);
                RenderedStagePropDataMap stagePropData = sequencePersistenceService.getRenderedStagePropsBySequenceAndSong(sequence, song);
                Map<String, UUID> stagePropUuids = sequencePersistenceService.getSequenceStagePropUuidMapBySequenceId(sequence.getId());
                SongAudio songAudio = sequencePersistenceService.getSongAudioBySequenceId(sequence.getId()).orElse(null);

                return new PlaybackState(playlist, playlistIndex, musicPlayerService::getSequenceProgress, sequence, song, songAudio, stagePropData, stagePropUuids);
            }
        } finally {
            unitOfWork.end();
        }
    }

    @Override
    public void stopPlayback() {
        logger.info("Stopping playback.");
        playbackState.set(new PlaybackState());
        musicPlayerService.stopPlayback();
    }
}

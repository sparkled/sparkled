package io.sparkled.music.impl

import com.google.common.util.concurrent.ThreadFactoryBuilder
import com.google.inject.persist.UnitOfWork
import io.sparkled.model.entity.Playlist
import io.sparkled.model.entity.Sequence
import io.sparkled.model.entity.Song
import io.sparkled.model.entity.SongAudio
import io.sparkled.model.render.RenderedStagePropDataMap
import io.sparkled.music.MusicPlayerService
import io.sparkled.music.PlaybackService
import io.sparkled.music.PlaybackState
import io.sparkled.music.PlaybackStateService
import io.sparkled.persistence.playlist.PlaylistPersistenceService
import io.sparkled.persistence.sequence.SequencePersistenceService
import io.sparkled.persistence.song.SongPersistenceService
import org.slf4j.Logger
import org.slf4j.LoggerFactory

import javax.inject.Inject
import javax.sound.sampled.LineEvent
import java.util.UUID
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import java.util.concurrent.atomic.AtomicReference

class PlaybackServiceImpl @Inject
constructor(private val songPersistenceService: SongPersistenceService,
            private val sequencePersistenceService: SequencePersistenceService,
            private val playlistPersistenceService: PlaylistPersistenceService,
            private val unitOfWork: UnitOfWork,
            private val musicPlayerService: MusicPlayerService) : PlaybackService, PlaybackStateService {
    private val executor: ExecutorService

    private val playbackState = AtomicReference<PlaybackState>(PlaybackState())

    init {

        this.executor = Executors.newSingleThreadScheduledExecutor(
                ThreadFactoryBuilder().setNameFormat("playback-service-%d").build()
        )

        musicPlayerService.addLineListener(LineListener { this.onLineEvent(it) })
    }

    private fun onLineEvent(event: LineEvent) {
        if (event.type === LineEvent.Type.STOP) {
            val state = this.playbackState.get()
            if (!state.isEmpty) {
                val playlist = state.playlist
                val playlistIndex = state.playlistIndex
                submitSequencePlayback(playlist, playlistIndex + 1)
            }
        }
    }

    override fun getPlaybackState(): PlaybackState {
        return playbackState.get()
    }

    override fun play(playlist: Playlist) {
        stopPlayback()

        logger.info("Playing playlist {}.", playlist.getName())
        submitSequencePlayback(playlist, 0)
    }

    private fun submitSequencePlayback(playlist: Playlist, playlistIndex: Int) {
        executor.submit { playSequenceAtIndex(playlist, playlistIndex) }
    }

    private fun playSequenceAtIndex(playlist: Playlist, playlistIndex: Int) {
        val playbackState = loadPlaybackState(playlist, playlistIndex)
        this.playbackState.set(playbackState)

        if (!playbackState.isEmpty) {
            musicPlayerService.play(playbackState)
        } else if (playlistIndex > 0) {
            logger.info("Finished playlist {}, restarting.", playlist.getId())
            playSequenceAtIndex(playlist, 0)
        } else {
            logger.error("Failed to play playlist {}: playlist is empty.", playlist.getId())
        }
    }

    private fun loadPlaybackState(playlist: Playlist, playlistIndex: Int): PlaybackState {
        try {
            unitOfWork.begin()
            val sequence = playlistPersistenceService.getSequenceAtPlaylistIndex(playlist.getId(), playlistIndex).orElse(null)

            if (sequence == null) {
                return PlaybackState()
            } else {
                val song = songPersistenceService.getSongBySequenceId(sequence!!.getId()).orElse(null)
                val stagePropData = sequencePersistenceService.getRenderedStagePropsBySequenceAndSong(sequence, song)
                val stagePropUuids = sequencePersistenceService.getSequenceStagePropUuidMapBySequenceId(sequence!!.getId())
                val songAudio = sequencePersistenceService.getSongAudioBySequenceId(sequence!!.getId()).orElse(null)

                return PlaybackState(playlist, playlistIndex, Supplier<Double> { musicPlayerService.getSequenceProgress() }, sequence, song, songAudio, stagePropData, stagePropUuids)
            }
        } finally {
            unitOfWork.end()
        }
    }

    override fun stopPlayback() {
        logger.info("Stopping playback.")
        playbackState.set(PlaybackState())
        musicPlayerService.stopPlayback()
    }

    companion object {

        private val logger = LoggerFactory.getLogger(PlaybackServiceImpl::class.java!!)
    }
}

package io.sparkled.music.impl

import com.google.common.util.concurrent.ThreadFactoryBuilder
import io.micronaut.spring.tx.annotation.Transactional
import io.sparkled.model.entity.Playlist
import io.sparkled.music.MusicPlayerService
import io.sparkled.music.PlaybackService
import io.sparkled.music.PlaybackState
import io.sparkled.music.PlaybackStateService
import io.sparkled.persistence.playlist.PlaylistPersistenceService
import io.sparkled.persistence.sequence.SequencePersistenceService
import io.sparkled.persistence.song.SongPersistenceService
import io.sparkled.persistence.stage.StagePersistenceService
import org.slf4j.LoggerFactory
import java.util.concurrent.Executors
import java.util.concurrent.atomic.AtomicReference
import javax.inject.Singleton
import javax.sound.sampled.LineEvent
import javax.sound.sampled.LineListener

@Singleton
open class PlaybackServiceImpl(
    private val songPersistenceService: SongPersistenceService,
    private val sequencePersistenceService: SequencePersistenceService,
    private val stagePersistenceService: StagePersistenceService,
    private val playlistPersistenceService: PlaylistPersistenceService,
    private val musicPlayerService: MusicPlayerService
) : PlaybackService, PlaybackStateService {
    private val playbackState = AtomicReference(PlaybackState())
    private val executor = Executors.newSingleThreadScheduledExecutor(
        ThreadFactoryBuilder().setNameFormat("playback-service-%d").build()
    )

    init {
        musicPlayerService.addLineListener(LineListener { this.onLineEvent(it) })
    }

    private fun onLineEvent(event: LineEvent) {
        if (event.type === LineEvent.Type.STOP) {
            val state = this.playbackState.get()
            if (!state.isEmpty) {
                val playlist = state.playlist!!
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
            logger.debug("Finished playlist {}, restarting.", playlist.getId())
            playSequenceAtIndex(playlist, 0)
        } else {
            logger.error("Failed to play playlist {}: playlist is empty.", playlist.getId())
        }
    }

    @Transactional(readOnly = true)
    open fun loadPlaybackState(playlist: Playlist, playlistIndex: Int): PlaybackState {
        try {
            val sequence = playlistPersistenceService.getSequenceAtPlaylistIndex(playlist.getId()!!, playlistIndex)

            return if (sequence == null) {
                PlaybackState()
            } else {
                val song = songPersistenceService.getSongBySequenceId(sequence.getId()!!)
                val songAudio = sequencePersistenceService.getSongAudioBySequenceId(sequence.getId()!!)

                val stagePropData = sequencePersistenceService.getRenderedStagePropsBySequenceAndSong(sequence, song!!)
                val stageProps = stagePersistenceService
                    .getStagePropsByStageId(sequence.getStageId()!!)
                    .associateBy({ it.getCode()!! }, { it })

                PlaybackState(
                    playlist = playlist,
                    playlistIndex = playlistIndex,
                    progressFunction = { musicPlayerService.sequenceProgress },
                    sequence = sequence,
                    song = song,
                    songAudio = songAudio,
                    renderedStageProps = stagePropData,
                    stageProps = stageProps
                )
            }
        } catch (e: Exception) {
            logger.error("Failed to load playback state.", e)
            return PlaybackState()
        }
    }

    override fun stopPlayback() {
        logger.info("Stopping playback.")
        playbackState.set(PlaybackState())
        musicPlayerService.stopPlayback()
    }

    companion object {
        private val logger = LoggerFactory.getLogger(PlaybackServiceImpl::class.java)
    }
}

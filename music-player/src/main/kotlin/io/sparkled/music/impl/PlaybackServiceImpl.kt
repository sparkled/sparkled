package io.sparkled.music.impl

import com.google.common.util.concurrent.ThreadFactoryBuilder
import io.micronaut.spring.tx.annotation.Transactional
import io.sparkled.model.entity.Sequence
import io.sparkled.music.MusicPlayerService
import io.sparkled.music.PlaybackService
import io.sparkled.music.PlaybackState
import io.sparkled.music.PlaybackStateService
import io.sparkled.persistence.sequence.SequencePersistenceService
import io.sparkled.persistence.song.SongPersistenceService
import io.sparkled.persistence.stage.StagePersistenceService
import java.util.concurrent.Executors
import java.util.concurrent.atomic.AtomicReference
import javax.inject.Singleton
import javax.sound.sampled.LineEvent
import javax.sound.sampled.LineListener
import org.slf4j.LoggerFactory

@Singleton
open class PlaybackServiceImpl(
    private val songPersistenceService: SongPersistenceService,
    private val sequencePersistenceService: SequencePersistenceService,
    private val stagePersistenceService: StagePersistenceService,
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
                val sequences = state.sequences!!
                val sequenceIndex = state.sequenceIndex
                submitSequencePlayback(sequences, sequenceIndex + 1, state.repeat)
            }
        }
    }

    override fun getPlaybackState(): PlaybackState {
        return playbackState.get()
    }

    override fun play(sequences: List<Sequence>, repeat: Boolean) {
        stopPlayback()
        submitSequencePlayback(sequences, 0, repeat)
    }

    private fun submitSequencePlayback(sequences: List<Sequence>, sequenceIndex: Int, repeat: Boolean) {
        executor.submit { playSequenceAtIndex(sequences, sequenceIndex, repeat) }
    }

    private fun playSequenceAtIndex(sequences: List<Sequence>, sequenceIndex: Int, repeat: Boolean) {
        val playbackState = loadPlaybackState(sequences, sequenceIndex, repeat)
        this.playbackState.set(playbackState)

        if (!playbackState.isEmpty) {
            musicPlayerService.play(playbackState)
        } else if (sequenceIndex > 0) {
            if (playbackState.repeat) {
                logger.debug("Finished playlist, restarting.")
                playSequenceAtIndex(sequences, 0, playbackState.repeat)
            }
        } else {
            logger.error("Failed to play empty playlist.")
        }
    }

    @Transactional(readOnly = true)
    open fun loadPlaybackState(sequences: List<Sequence>, sequenceIndex: Int, repeat: Boolean): PlaybackState {
        try {
            if (sequenceIndex >= sequences.size) {
                return PlaybackState(repeat = repeat)
            }

            val sequence = sequences[sequenceIndex]

            val song = songPersistenceService.getSongBySequenceId(sequence.getId()!!)
            val songAudio = sequencePersistenceService.getSongAudioBySequenceId(sequence.getId()!!)

            val stagePropData = sequencePersistenceService.getRenderedStagePropsBySequenceAndSong(sequence, song!!)
            val stageProps = stagePersistenceService
                .getStagePropsByStageId(sequence.getStageId()!!)
                .associateBy({ it.getCode()!! }, { it })

            return PlaybackState(
                sequences = sequences,
                sequenceIndex = sequenceIndex,
                repeat = repeat,
                progressFunction = { musicPlayerService.sequenceProgress },
                sequence = sequence,
                song = song,
                songAudio = songAudio,
                renderedStageProps = stagePropData,
                stageProps = stageProps
            )
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

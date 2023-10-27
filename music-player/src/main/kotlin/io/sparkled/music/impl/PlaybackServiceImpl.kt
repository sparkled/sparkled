package io.sparkled.music.impl

import com.google.common.util.concurrent.ThreadFactoryBuilder
import io.sparkled.model.SequenceModel
import io.sparkled.model.render.RenderedStagePropData
import io.sparkled.model.render.RenderedStagePropDataMap
import io.sparkled.music.MusicPlayerService
import io.sparkled.music.PlaybackService
import io.sparkled.music.PlaybackState
import io.sparkled.music.PlaybackStateService
import io.sparkled.persistence.DbService
import io.sparkled.persistence.FileService
import io.sparkled.persistence.query.sequence.GetSongBySequenceIdQuery
import io.sparkled.persistence.query.stage.GetStagePropsByStageIdQuery
import jakarta.inject.Singleton
import org.slf4j.LoggerFactory
import org.springframework.transaction.annotation.Transactional
import java.util.*
import java.util.concurrent.Executors
import java.util.concurrent.atomic.AtomicReference
import javax.sound.sampled.LineEvent

@Singleton
class PlaybackServiceImpl(
    private val db: DbService,
    private val file: FileService,
    private val musicPlayerService: MusicPlayerService
) : PlaybackService, PlaybackStateService {
    private val playbackState = AtomicReference(PlaybackState())

    // TODO remove Guava. Try Micronaut's NamedThreadFactory class.
    private val executor = Executors.newSingleThreadScheduledExecutor(
        ThreadFactoryBuilder().setNameFormat("playback-service-%d").build()
    )

    init {
        musicPlayerService.addLineListener { this.onLineEvent(it) }
    }

    private fun onLineEvent(event: LineEvent) {
        if (event.type === LineEvent.Type.STOP) {
            val state = this.playbackState.get()
            if (!state.isEmpty) {
                val sequences = state.sequences
                val sequenceIndex = state.sequenceIndex
                submitSequencePlayback(sequences, sequenceIndex + 1, state.repeat)
            }
        }
    }

    override fun getPlaybackState(): PlaybackState {
        return playbackState.get()
    }

    override fun play(sequences: List<SequenceModel>, repeat: Boolean) {
        synchronized(this) {
            stopPlayback()
            submitSequencePlayback(sequences, 0, repeat)
        }
    }

    private fun submitSequencePlayback(sequences: List<SequenceModel>, sequenceIndex: Int, repeat: Boolean) {
        executor.submit { playSequenceAtIndex(sequences, sequenceIndex, repeat) }
    }

    private fun playSequenceAtIndex(sequences: List<SequenceModel>, sequenceIndex: Int, repeat: Boolean) {
        val playbackState = loadPlaybackState(sequences, sequenceIndex, repeat)
        this.playbackState.set(playbackState)

        if (!playbackState.isEmpty) {
            musicPlayerService.play(playbackState)
        } else if (sequenceIndex > 0) {
            if (playbackState.repeat) {
                logger.debug("Finished playlist, restarting.")
                playSequenceAtIndex(sequences, 0, true)
            }
        } else {
            logger.error("Failed to play empty playlist.")
        }
    }

    @Transactional
    fun loadPlaybackState(sequences: List<SequenceModel>, sequenceIndex: Int, repeat: Boolean): PlaybackState {
        try {
            if (sequenceIndex >= sequences.size) {
                return PlaybackState(repeat = repeat)
            }

            val sequence = sequences[sequenceIndex]
            val song = db.songs.findBySequenceId(sequence.id)!!
            val songAudio = file.readSongAudio(song.id)

            val render = file.readRender(sequence.id)
            val stagePropData = render.stageProps
                .mapValuesTo(RenderedStagePropDataMap()) { (_, value) ->
                    RenderedStagePropData(
                        startFrame = 0,
                        endFrame = render.startFrame + render.frameCount,
                        ledCount = value.ledCount,
                        data = Base64.getDecoder().decode(value.base64Data),
                    )
                }
            val stageProps = db.stageProps.findAllByStageId(sequence.stageId).associateBy { it.code }

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
            return PlaybackState.empty
        }
    }

    override fun stopPlayback() {
        synchronized(this) {
            logger.info("Stopping playback.")
            playbackState.set(PlaybackState.empty)
            musicPlayerService.stopPlayback()
        }
    }

    companion object {
        private val logger = LoggerFactory.getLogger(PlaybackServiceImpl::class.java)
    }
}

package io.sparkled.music.impl

import io.micronaut.transaction.annotation.Transactional
import io.sparkled.common.logging.getLogger
import io.sparkled.common.threading.NamedVirtualThreadFactory
import io.sparkled.model.SequenceModel
import io.sparkled.model.StageModel
import io.sparkled.model.StagePropModel
import io.sparkled.model.render.RenderedStagePropData
import io.sparkled.model.render.RenderedStagePropDataMap
import io.sparkled.music.InteractivePlaybackState
import io.sparkled.music.MusicPlayerService
import io.sparkled.music.PlaybackService
import io.sparkled.music.PlaybackState
import io.sparkled.music.PlaybackStateService
import io.sparkled.music.SequencePlaybackState
import io.sparkled.music.StoppedPlaybackState
import io.sparkled.persistence.DbService
import io.sparkled.persistence.FileService
import io.sparkled.persistence.cache.CacheService
import jakarta.inject.Singleton
import java.nio.ByteBuffer
import java.util.Base64
import java.util.concurrent.Executors
import java.util.concurrent.atomic.AtomicReference
import javax.sound.sampled.LineEvent

@Singleton
class PlaybackServiceImpl(
    private val cache: CacheService,
    private val db: DbService,
    private val fileService: FileService,
    private val musicPlayerService: MusicPlayerService,
) : PlaybackService, PlaybackStateService {
    private val playbackState = AtomicReference<PlaybackState>(StoppedPlaybackState)

    private val executor = Executors.newSingleThreadScheduledExecutor(
        NamedVirtualThreadFactory(javaClass.simpleName),
    )

    init {
        musicPlayerService.addLineListener { this.onLineEvent(it) }
    }

    override val state: PlaybackState
        get() = playbackState.get()

    private fun onLineEvent(event: LineEvent) {
        if (event.type === LineEvent.Type.STOP) {
            val state = this.playbackState.get()
            if (state is SequencePlaybackState) {
                val sequences = state.sequences
                val sequenceIndex = state.sequenceIndex
                submitSequencePlayback(sequences, sequenceIndex + 1, state.repeat)
            }
        }
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

        if (playbackState is SequencePlaybackState) {
            musicPlayerService.play(playbackState)
        }
    }

    @Transactional
    fun loadPlaybackState(sequences: List<SequenceModel>, sequenceIndex: Int, repeat: Boolean): PlaybackState {
        try {
            val currentPlaybackState = state

            if (sequenceIndex >= sequences.size) {
                return if (currentPlaybackState is SequencePlaybackState && currentPlaybackState.repeat) {
                    logger.debug("Finished playlist, restarting.")
                    currentPlaybackState.copy(sequenceIndex = 0)
                } else {
                    StoppedPlaybackState
                }
            }

            val sequence = sequences[sequenceIndex]
            val song = db.songs.findBySequenceId(sequence.id)!!
            val songAudio = cache.songAudios.get()[song.id]

            val render = fileService.readRender(sequence.id)
            val stagePropData = render.stageProps
                .mapValuesTo(RenderedStagePropDataMap()) { (_, value) ->
                    RenderedStagePropData(
                        startFrame = 0,
                        endFrame = render.startFrame + render.frameCount,
                        ledCount = value.ledCount,
                        data = Base64.getDecoder().decode(value.base64Data),
                    )
                }
            val stageProps = db.stageProps.findAllByStageId(sequence.stageId).associateBy { it.id }

            return SequencePlaybackState(
                sequences = sequences,
                sequenceIndex = sequenceIndex,
                repeat = repeat,
                progressFunction = { musicPlayerService.sequenceProgress },
                song = song,
                songAudio = ByteBuffer.wrap(songAudio),
                renderedStageProps = stagePropData,
                stageProps = stageProps,
            )
        } catch (e: Exception) {
            logger.error("Failed to load playback state.", e)
            return StoppedPlaybackState
        }
    }

    override fun stopPlayback() {
        synchronized(this) {
            logger.info("Stopping playback.")
            playbackState.set(StoppedPlaybackState)
            musicPlayerService.stopPlayback()
        }
    }

    override fun disableInteractiveMode() {
        val currentState = state
        if (currentState is InteractivePlaybackState) {
            val previousState = currentState.previousState
            playbackState.set(previousState)

            if (previousState is SequencePlaybackState) {
                val sequences = previousState.sequences
                val sequenceIndex = previousState.sequenceIndex
                submitSequencePlayback(sequences, sequenceIndex + 1, previousState.repeat)
            }
        }
    }

    override fun enableInteractiveMode(stage: StageModel, stageProps: List<StagePropModel>) {
        val currentState = state
        if (currentState !is InteractivePlaybackState) {
            playbackState.set(
                InteractivePlaybackState(
                    stage = stage,
                    renderedStageProps = stageProps
                        .groupBy { if ( it.groupCode.isNullOrBlank()) it.code else it.groupCode!! }
                        .mapValuesTo(RenderedStagePropDataMap()) { (_, stageProps) ->
                            val ledCount = stageProps.sumOf { it.ledCount }
                            RenderedStagePropData(
                                startFrame = 0,
                                endFrame = 1,
                                ledCount = ledCount,
                                data = ByteArray(ledCount * 3),
                            )
                        },
                    stageProps = stageProps.associateBy { it.id },
                    previousState = currentState,
                )
            )

            musicPlayerService.stopPlayback()
        }
    }

    companion object {
        private val logger = getLogger<PlaybackServiceImpl>()
    }
}

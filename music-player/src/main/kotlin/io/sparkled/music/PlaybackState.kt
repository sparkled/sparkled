package io.sparkled.music

import io.sparkled.model.SequenceModel
import io.sparkled.model.SongModel
import io.sparkled.model.StageModel
import io.sparkled.model.StagePropModel
import io.sparkled.model.UniqueId
import io.sparkled.model.animation.effect.Effect
import io.sparkled.model.render.RenderedStagePropDataMap
import io.sparkled.model.util.SequenceUtils
import java.lang.System.currentTimeMillis
import java.nio.ByteBuffer
import java.util.concurrent.ConcurrentHashMap

sealed interface PlaybackState {
    val stageProps: Map<String, StagePropModel>
    val renderedStageProps: RenderedStagePropDataMap
    val frameCount: Int
    val progress: Double
    val framesPerSecond: Int
}

data object StoppedPlaybackState : PlaybackState {
    override val stageProps = emptyMap<String, StagePropModel>()
    override val renderedStageProps = RenderedStagePropDataMap()
    override val frameCount = 0
    override val progress = 0.0
    override val framesPerSecond = 1
}

data class InteractivePlaybackState(
    override var renderedStageProps: RenderedStagePropDataMap,
    override val stageProps: Map<UniqueId, StagePropModel>,
    val previousState: PlaybackState = StoppedPlaybackState,
    val stage: StageModel,
    val stagePropEffects: ConcurrentHashMap<UniqueId, MutableList<Effect>> = ConcurrentHashMap()
) : PlaybackState {
    override val frameCount = 1
    override val progress = 0.0
    override val framesPerSecond = 30

    private val startTime = currentTimeMillis()
    val startFrame = getFrameAtCurrentTime()

    fun getFrameAtCurrentTime() = (currentTimeMillis() - startTime).toInt() / framesPerSecond

    init {
        stageProps.keys.forEach {
            stagePropEffects[it] = mutableListOf()
        }
    }
}

/**
 * A container object holding all the information pertaining to the current state of playback, in terms of audio
 * playback and associated rendered data for streaming to clients.
 */
data class SequencePlaybackState(
    val sequences: List<SequenceModel> = emptyList(),
    val sequenceIndex: Int = 0,
    val repeat: Boolean = true,
    private val progressFunction: () -> Double = { 0.0 },
    val song: SongModel,
    val songAudio: ByteBuffer = ByteBuffer.allocate(0),
    override val renderedStageProps: RenderedStagePropDataMap = RenderedStagePropDataMap(),
    override val stageProps: Map<UniqueId, StagePropModel> = emptyMap()
) : PlaybackState {
    val sequence: SequenceModel
        get() = sequences[sequenceIndex]

    override val framesPerSecond: Int
        get() = sequence.framesPerSecond

    override val frameCount: Int
        get() = SequenceUtils.getFrameCount(song, sequence)

    override val progress: Double
        get() = progressFunction.invoke()
}

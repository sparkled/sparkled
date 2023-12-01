package io.sparkled.music

import io.sparkled.model.SequenceModel
import io.sparkled.model.SongModel
import io.sparkled.model.StagePropModel
import io.sparkled.model.render.RenderedStagePropDataMap
import io.sparkled.model.util.SequenceUtils
import java.nio.ByteBuffer

sealed interface PlaybackState {
    val stageProps: Map<String, StagePropModel>
    val renderedStageProps: RenderedStagePropDataMap
    val frameCount: Int
    val progress: Double
}

data object StoppedPlaybackState : PlaybackState {
    override val stageProps = emptyMap<String, StagePropModel>()
    override val renderedStageProps = RenderedStagePropDataMap()
    override val frameCount = 0
    override val progress = 0.0
}

data class InteractivePlaybackState(
    override val renderedStageProps: RenderedStagePropDataMap,
    override val stageProps: Map<String, StagePropModel>,
) : PlaybackState {
    override val frameCount = 1
    override val progress = 0.0
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
    override val stageProps: Map<String, StagePropModel> = emptyMap()
) : PlaybackState {
    val sequence: SequenceModel
        get() = sequences[sequenceIndex]

    override val frameCount: Int
        get() = SequenceUtils.getFrameCount(song, sequence)

    override val progress: Double
        get() = progressFunction.invoke()
}

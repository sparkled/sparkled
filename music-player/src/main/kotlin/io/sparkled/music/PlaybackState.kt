package io.sparkled.music

import io.sparkled.model.SequenceModel
import io.sparkled.model.SongModel
import io.sparkled.model.StagePropModel
import io.sparkled.model.render.RenderedStagePropDataMap

/**
 * A container object holding all the information pertaining to the current state of playback, in terms of audio
 * playback and associated rendered data for streaming to clients.
 */
data class PlaybackState(
    val sequences: List<SequenceModel> = emptyList(),
    val sequenceIndex: Int = 0,
    val repeat: Boolean = true,
    private val progressFunction: () -> Double = { 0.0 },
    val sequence: SequenceModel? = null,
    val song: SongModel? = null,
    val songAudio: ByteArray = byteArrayOf(),
    val renderedStageProps: RenderedStagePropDataMap = RenderedStagePropDataMap(),
    val stageProps: Map<String, StagePropModel> = emptyMap()
) {

    val isEmpty: Boolean
        get() = sequences.isEmpty() || sequence == null || song == null || songAudio.isEmpty() || renderedStageProps.isEmpty()

    val progress: Double
        get() = progressFunction.invoke()

    companion object {
        val empty = PlaybackState()
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as PlaybackState

        if (sequences != other.sequences) return false
        if (sequenceIndex != other.sequenceIndex) return false
        if (repeat != other.repeat) return false
        if (progressFunction != other.progressFunction) return false
        if (sequence != other.sequence) return false
        if (song != other.song) return false
        if (!songAudio.contentEquals(other.songAudio)) return false
        if (renderedStageProps != other.renderedStageProps) return false
        if (stageProps != other.stageProps) return false

        return true
    }

    override fun hashCode(): Int {
        var result = sequences.hashCode()
        result = 31 * result + sequenceIndex
        result = 31 * result + repeat.hashCode()
        result = 31 * result + progressFunction.hashCode()
        result = 31 * result + (sequence?.hashCode() ?: 0)
        result = 31 * result + (song?.hashCode() ?: 0)
        result = 31 * result + songAudio.contentHashCode()
        result = 31 * result + (renderedStageProps.hashCode())
        result = 31 * result + stageProps.hashCode()
        return result
    }
}

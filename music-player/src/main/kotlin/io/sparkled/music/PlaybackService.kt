package io.sparkled.music

import io.sparkled.model.SequenceModel
import io.sparkled.model.StageModel
import io.sparkled.model.StagePropModel

/**
 * Plays and stops playlists.
 */
interface PlaybackService {

    /**
     * Begins playback of the provided sequences, on repeat.

     * @param sequences The sequences to be played
     */
    fun play(sequences: List<SequenceModel>, repeat: Boolean)

    /**
     * Stops playback of the current playlist. If no playlist is playing, this is a no-op.
     */
    fun stopPlayback()

    val state: PlaybackState

    fun disableInteractiveMode()

    fun enableInteractiveMode(stage: StageModel, stageProps: List<StagePropModel>)
}

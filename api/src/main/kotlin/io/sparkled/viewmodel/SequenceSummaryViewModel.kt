package io.sparkled.viewmodel

import io.sparkled.model.SequenceModel
import io.sparkled.model.SongModel
import io.sparkled.model.StageModel
import io.sparkled.model.UniqueId
import io.sparkled.model.constant.ModelConstants
import io.sparkled.model.enumeration.SequenceStatus
import io.sparkled.model.util.IdUtils

data class SequenceSummaryViewModel(
    val id: UniqueId,
    val name: String,
    val songName: String,
    val stageName: String,
    val framesPerSecond: Int,
    val durationSeconds: Int,
    val status: SequenceStatus,
) : ViewModel {
    companion object {
        fun fromModel(
            model: SequenceModel,
            song: SongModel,
            stage: StageModel,
        ): SequenceSummaryViewModel {
            return SequenceSummaryViewModel(
                id = model.id,
                name = model.name,
                songName = song.name,
                stageName = stage.name,
                framesPerSecond = model.framesPerSecond,
                durationSeconds = song.durationMs.div(ModelConstants.MS_PER_SECOND),
                status = model.status,
            )
        }
    }
}
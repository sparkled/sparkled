package io.sparkled.viewmodel

import io.sparkled.model.constant.ModelConstants
import io.sparkled.model.entity.SequenceStatus
import io.sparkled.model.entity.v2.SequenceEntity
import io.sparkled.model.entity.v2.SongEntity
import io.sparkled.model.entity.v2.StageEntity
import io.sparkled.model.util.IdUtils

data class SequenceSummaryViewModel(
    val id: Int = IdUtils.NO_ID,
    val name: String,
    val songName: String,
    val stageName: String,
    val framesPerSecond: Int,
    val durationSeconds: Int,
    val status: SequenceStatus,
) {
    companion object {
        fun fromModel(
            model: SequenceEntity,
            song: SongEntity,
            stage: StageEntity
        ): SequenceSummaryViewModel {
            return SequenceSummaryViewModel(
                id = model.id,
                name = model.name,
                songName = song.name,
                stageName = stage.name,
                framesPerSecond = model.framesPerSecond,
                durationSeconds = song.durationMs.div(ModelConstants.MS_PER_SECOND),
                status = model.status
            )
        }
    }
}

package io.sparkled.viewmodel

import io.sparkled.model.SequenceChannelModel
import io.sparkled.model.SequenceModel
import io.sparkled.model.SongModel
import io.sparkled.model.UniqueId
import io.sparkled.model.annotation.GenerateClientType
import io.sparkled.model.enumeration.SequenceStatus
import io.sparkled.model.util.SequenceUtils

@GenerateClientType
data class SequenceViewModel(
    val id: UniqueId,
    val songId: UniqueId,
    val stageId: UniqueId,
    val name: String,
    val framesPerSecond: Int,
    val frameCount: Int = 0,
    val status: SequenceStatus = SequenceStatus.NEW,
    val channels: List<SequenceChannelViewModel> = emptyList()
) : ViewModel {
    fun toModel(): Pair<SequenceModel, List<SequenceChannelModel>> {
        val sequence = SequenceModel(
            id = id,
            songId = songId,
            stageId = stageId,
            name = name,
            framesPerSecond = framesPerSecond,
            status = status,
        )

        val channels = channels.map { it.toModel(sequenceId = id) }
        return sequence to channels
    }

    companion object {
        fun fromModel(
            model: SequenceModel,
            song: SongModel,
            channels: Collection<SequenceChannelModel>,
        ) = SequenceViewModel(
            id = model.id,
            songId = model.songId,
            stageId = model.stageId,
            name = model.name,
            framesPerSecond = model.framesPerSecond,
            frameCount = SequenceUtils.getFrameCount(song, model),
            status = model.status,
            channels = channels.map(SequenceChannelViewModel::fromModel),
        )
    }
}

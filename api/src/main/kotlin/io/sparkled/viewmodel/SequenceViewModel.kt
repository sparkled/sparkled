package io.sparkled.viewmodel

import com.fasterxml.jackson.databind.ObjectMapper
import io.sparkled.model.enumeration.SequenceStatus
import io.sparkled.model.entity.v2.SequenceChannelEntity
import io.sparkled.model.entity.v2.SequenceEntity
import io.sparkled.model.entity.v2.SongEntity
import io.sparkled.model.util.IdUtils
import io.sparkled.model.util.SequenceUtils

data class SequenceViewModel(
    val id: Int = IdUtils.NO_ID,
    val songId: Int,
    val stageId: Int,
    val name: String,
    val framesPerSecond: Int,
    val frameCount: Int = 0,
    val status: SequenceStatus = SequenceStatus.NEW,
    val channels: List<SequenceChannelViewModel> = emptyList()
) {
    fun toModel(objectMapper: ObjectMapper): Pair<SequenceEntity, List<SequenceChannelEntity>> {
        val sequence = SequenceEntity(
            id = id,
            songId = songId,
            stageId = stageId,
            name = name,
            framesPerSecond = framesPerSecond,
            status = status,
        )

        val channels = channels.map { it.toModel(sequenceId = id, objectMapper) }
        return sequence to channels
    }

    companion object {
        fun fromModel(
            model: SequenceEntity,
            song: SongEntity,
            channels: List<SequenceChannelEntity>,
            objectMapper: ObjectMapper
        ) = SequenceViewModel(
            id = model.id,
            songId = model.songId,
            stageId = model.stageId,
            name = model.name,
            framesPerSecond = model.framesPerSecond,
            frameCount = SequenceUtils.getFrameCount(song, model),
            status = model.status,
            channels = channels.map { SequenceChannelViewModel.fromModel(it, objectMapper) }
        )
    }
}

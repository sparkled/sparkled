package io.sparkled.viewmodel

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import io.sparkled.model.animation.effect.Effect
import io.sparkled.model.entity.v2.SequenceChannelEntity
import io.sparkled.model.util.IdUtils
import java.util.UUID

data class SequenceChannelViewModel(
    val uuid: UUID = IdUtils.newUuid(),
    val stagePropUuid: UUID,
    val name: String,
    val displayOrder: Int,
    val effects: List<Effect>
) {

    fun toModel(sequenceId: Int, objectMapper: ObjectMapper) = SequenceChannelEntity(
        uuid = uuid,
        sequenceId = sequenceId,
        stagePropUuid = stagePropUuid,
        name = name,
        displayOrder = displayOrder,
        channelJson = objectMapper.writeValueAsString(effects)
    )

    companion object {
        fun fromModel(model: SequenceChannelEntity, objectMapper: ObjectMapper) = SequenceChannelViewModel(
            uuid = model.uuid,
            stagePropUuid = model.stagePropUuid,
            name = model.name,
            displayOrder = model.displayOrder,
            effects = objectMapper.readValue(model.channelJson)
        )
    }
}

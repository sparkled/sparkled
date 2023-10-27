package io.sparkled.viewmodel

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import io.sparkled.model.SequenceChannelModel
import io.sparkled.model.UniqueId
import io.sparkled.model.animation.effect.Effect
import io.sparkled.model.util.IdUtils.uniqueId

data class SequenceChannelViewModel(
    val id: String = uniqueId(),
    val stagePropId: UniqueId,
    val name: String,
    val displayOrder: Int,
    val effects: List<Effect>,
) : ViewModel {

    fun toModel(sequenceId: UniqueId, objectMapper: ObjectMapper) = SequenceChannelModel(
        id = id,
        sequenceId = sequenceId,
        stagePropId = stagePropId,
        name = name,
        displayOrder = displayOrder,
        channelJson = objectMapper.writeValueAsString(effects),
    )

    companion object {
        fun fromModel(model: SequenceChannelModel, objectMapper: ObjectMapper) = SequenceChannelViewModel(
            id = model.id,
            stagePropId = model.stagePropId,
            name = model.name,
            displayOrder = model.displayOrder,
            effects = objectMapper.readValue(model.channelJson),
        )
    }
}

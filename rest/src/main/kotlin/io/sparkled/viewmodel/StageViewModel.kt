package io.sparkled.viewmodel

import com.fasterxml.jackson.databind.ObjectMapper
import io.sparkled.model.entity.v2.*
import io.sparkled.model.util.IdUtils

data class StageViewModel(
    val id: Int = IdUtils.NO_ID,
    val name: String,
    val width: Int,
    val height: Int,
    val stageProps: List<StagePropViewModel> = emptyList()
) {
    fun toModel(objectMapper: ObjectMapper): Pair<StageEntity, List<StagePropEntity>> {
        val stage = StageEntity(
            id = id,
            name = name,
            width = width,
            height = height
        )

        val stageProps = stageProps.map { it.toModel(objectMapper) }
        return stage to stageProps
    }

    companion object {
        fun fromModel(model: StageEntity, stageProps: List<StagePropEntity>, objectMapper: ObjectMapper) = StageViewModel(
            id = model.id,
            name = model.name,
            width = model.width,
            height = model.height,
            stageProps = stageProps.map { StagePropViewModel.fromModel(it, objectMapper) }
        )
    }
}

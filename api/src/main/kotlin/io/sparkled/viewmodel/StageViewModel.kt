package io.sparkled.viewmodel

import io.sparkled.model.StageModel
import io.sparkled.model.StagePropModel
import io.sparkled.model.UniqueId

data class StageViewModel(
    val id: UniqueId,
    val name: String,
    val width: Int,
    val height: Int,
    val stageProps: List<StagePropViewModel> = emptyList()
) : ViewModel {
    fun toModel(): Pair<StageModel, List<StagePropModel>> {
        val stage = StageModel(
            id = id,
            name = name,
            width = width,
            height = height,
        )

        val stageProps = stageProps.map(StagePropViewModel::toModel)
        return stage to stageProps
    }

    companion object {
        fun fromModel(
            model: StageModel,
            stageProps: Collection<StagePropModel>,
        ) = StageViewModel(
            id = model.id,
            name = model.name,
            width = model.width,
            height = model.height,
            stageProps = stageProps.map(StagePropViewModel::fromModel)
        )
    }
}

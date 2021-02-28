package io.sparkled.viewmodel

import io.sparkled.model.entity.v2.StageEntity

data class StageSummaryViewModel(
    val id: Int,
    val name: String,
) {
    companion object {
        fun fromModel(model: StageEntity) = StageSummaryViewModel(
            id = model.id,
            name = model.name,
        )
    }
}

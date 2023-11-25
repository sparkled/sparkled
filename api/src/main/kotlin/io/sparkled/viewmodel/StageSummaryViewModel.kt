package io.sparkled.viewmodel

import io.sparkled.model.StageModel
import io.sparkled.model.UniqueId
import io.sparkled.model.annotation.GenerateClientType

@GenerateClientType
data class StageSummaryViewModel(
    val id: UniqueId,
    val name: String,
) : ViewModel {
    companion object {
        fun fromModel(model: StageModel) = StageSummaryViewModel(
            id = model.id,
            name = model.name,
        )
    }
}

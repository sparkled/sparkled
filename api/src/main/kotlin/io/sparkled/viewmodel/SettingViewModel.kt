package io.sparkled.viewmodel

import io.sparkled.model.SettingModel
import io.sparkled.model.UniqueId
import io.sparkled.model.annotation.GenerateClientType

@GenerateClientType
data class SettingViewModel(
    val id: UniqueId,
    val value: String,
) : ViewModel {
    fun toModel() = SettingModel(
        id = id,
        value = value,
    )

    companion object {
        fun fromModel(model: SettingModel) = SettingViewModel(
            id = model.id,
            value = model.value,
        )
    }
}

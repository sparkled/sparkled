package io.sparkled.viewmodel

import io.sparkled.model.SettingModel
import io.sparkled.model.UniqueId

data class SettingViewModel(
    val id: UniqueId,
    val code: String,
    val value: String,
) : ViewModel {
    fun toModel() = SettingModel(
        id = id,
        code = code,
        value = value,
    )

    companion object {
        fun fromModel(model: SettingModel) = SettingViewModel(
            id = model.id,
            code = model.code,
            value = model.value,
        )
    }
}

package io.sparkled.viewmodel

import io.sparkled.model.entity.v2.SettingEntity

data class SettingViewModel(
    val code: String,
    val value: String,
) {
    fun toModel() = SettingEntity(
        code = code,
        value = value,
    )

    companion object {
        fun fromModel(model: SettingEntity) = SettingViewModel(
            code = model.code,
            value = model.value,
        )
    }
}

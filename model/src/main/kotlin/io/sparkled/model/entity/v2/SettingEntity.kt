package io.sparkled.model.entity.v2

import io.sparkled.model.annotation.Entity

@Entity(name = "setting", idField = "code")
data class SettingEntity(
    val code: String,
    val value: String,
) : SparkledEntity

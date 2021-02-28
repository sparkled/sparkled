package io.sparkled.model.setting

import io.sparkled.model.entity.v2.SettingEntity

/**
 * A cached copy of the contents of known [SettingEntity] records from the database.
 */
data class SettingsCache(val brightness: Int)

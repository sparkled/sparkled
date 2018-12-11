package io.sparkled.model.setting

import io.sparkled.model.entity.Setting

/**
 * A cached copy of the contents of known [Setting] records from the database.
 */
class SettingsCache(val brightness: Int)
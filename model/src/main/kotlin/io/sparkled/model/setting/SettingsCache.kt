package io.sparkled.model.setting

import io.sparkled.model.entity.Setting

/**
 * A cached copy of the contents of known [Setting] records from the database.
 */
class SettingsCache(brightness: Int) {

    /**
     * @return The brightness setting, as a value between 0 and 15, inclusive.
     */
    val brightness: Int = 0
}

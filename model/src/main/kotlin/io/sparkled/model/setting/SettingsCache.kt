package io.sparkled.model.setting

import io.sparkled.model.entity.Setting

/**
 * A cached copy of the contents of known [Setting] records from the database.
 */
class SettingsCache(val brightness: Int) {

    /**
     * The brightness setting, as a value between 0 and 15, inclusive.
     */
    val progress: Int
        get() = brightness
}

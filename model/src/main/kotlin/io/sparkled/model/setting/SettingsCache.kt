package io.sparkled.model.setting

import io.sparkled.model.entity.Setting

import java.util.Objects

/**
 * A cached copy of the contents of known [Setting] records from the database.
 */
class SettingsCache(
        /**
         * @return The brightness setting, as a value between 0 and 15, inclusive.
         */
        val brightness: Int) {

    @Override
    fun equals(o: Object?): Boolean {
        if (this === o) return true
        if (o == null || getClass() !== o!!.getClass()) return false
        return brightness == o!!.brightness
    }

    @Override
    fun hashCode(): Int {
        return Objects.hash(brightness)
    }

    @Override
    fun toString(): String {
        return "SettingsCache{" +
                "brightness=" + brightness +
                '}'
    }
}

package io.sparkled.model.setting;

import io.sparkled.model.entity.Setting;

import java.util.Objects;

/**
 * A cached copy of the contents of known {@link Setting} records from the database.
 */
public class SettingsCache {

    private int brightness;

    public SettingsCache(int brightness) {
        this.brightness = brightness;
    }

    /**
     * @return The brightness setting, as a value between 0 and 15, inclusive.
     */
    public int getBrightness() {
        return brightness;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SettingsCache that = (SettingsCache) o;
        return brightness == that.brightness;
    }

    @Override
    public int hashCode() {
        return Objects.hash(brightness);
    }

    @Override
    public String toString() {
        return "SettingsCache{" +
                "brightness=" + brightness +
                '}';
    }
}

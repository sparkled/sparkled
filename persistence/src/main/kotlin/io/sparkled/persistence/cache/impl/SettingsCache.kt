package io.sparkled.persistence.cache.impl

import io.sparkled.model.setting.SettingsCacheEntry
import io.sparkled.model.setting.SettingsConstants
import io.sparkled.persistence.DbService
import io.sparkled.persistence.cache.Cache
import jakarta.inject.Singleton
import java.time.Instant

@Singleton
class SettingsCache(
    private val db: DbService,
) : Cache<SettingsCacheEntry>(
    name = "Settings",
    fallback = SettingsCacheEntry(
        brightness = SettingsConstants.Brightness.MAX,
        framesPerSecond = SettingsConstants.FramesPerSecond.DEFAULT,
    ),
) {
    override fun reload(lastLoadedAt: Instant?): SettingsCacheEntry {
        val settings = db.settings.findAll()
        val brightness = settings.firstOrNull { it.id == SettingsConstants.Brightness.CODE }
        val framesPerSecond = settings.firstOrNull { it.id == SettingsConstants.FramesPerSecond.CODE }

        return SettingsCacheEntry(
            brightness = brightness?.value?.toInt() ?: SettingsConstants.Brightness.MAX,
            framesPerSecond = framesPerSecond?.value?.toInt() ?: SettingsConstants.FramesPerSecond.DEFAULT,
        )
    }
}

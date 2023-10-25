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
    fallback = SettingsCacheEntry(brightness = SettingsConstants.Brightness.MAX),
) {
    override fun reload(lastLoadedAt: Instant?): SettingsCacheEntry {
        val brightness = db.settings.findAll().firstOrNull { it.id == SettingsConstants.Brightness.CODE }
        return SettingsCacheEntry(brightness = brightness?.value?.toInt() ?: SettingsConstants.Brightness.MAX)
    }
}

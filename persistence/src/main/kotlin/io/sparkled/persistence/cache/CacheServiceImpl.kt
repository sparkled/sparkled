package io.sparkled.persistence.cache

import io.sparkled.model.entity.v2.SettingEntity
import io.sparkled.model.setting.SettingsCache
import io.sparkled.model.setting.SettingsConstants
import io.sparkled.persistence.DbService
import io.sparkled.persistence.getAll
import javax.inject.Singleton

@Singleton
class CacheServiceImpl(
    db: DbService,
) : CacheService {

    override val settings = Cache(
        name = "Settings",
        fallback = SettingsCache(brightness = SettingsConstants.Brightness.MAX),
    ) {
        val brightness = db.getAll<SettingEntity>().firstOrNull { it.code == SettingsConstants.Brightness.CODE }
        SettingsCache(brightness = brightness?.value?.toInt() ?: SettingsConstants.Brightness.MAX)
    }

    override val allCaches = listOf(settings)
}

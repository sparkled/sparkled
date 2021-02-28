package io.sparkled.persistence.cache

import io.sparkled.model.setting.SettingsCache


interface CacheService {
    val settings: Cache<SettingsCache>
    val allCaches: List<Cache<SettingsCache>>
}


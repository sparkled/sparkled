package io.sparkled.persistence.cache

import io.sparkled.model.setting.SettingsCache
import java.awt.image.BufferedImage


interface CacheService {
    val gifs: Cache<LinkedHashMap<String, List<BufferedImage>>>
    val settings: Cache<SettingsCache>
    val allCaches: List<Cache<*>>
}


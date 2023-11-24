package io.sparkled.persistence.cache

import io.sparkled.persistence.cache.impl.GifsCache
import io.sparkled.persistence.cache.impl.SettingsCache

interface CacheService {
    val gifs: GifsCache
    val settings: SettingsCache
    val allCaches: List<Cache<*>>
}


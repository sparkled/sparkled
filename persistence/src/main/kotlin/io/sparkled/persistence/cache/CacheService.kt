package io.sparkled.persistence.cache

import io.sparkled.persistence.cache.impl.GifsCache
import io.sparkled.persistence.cache.impl.SettingsCache
import io.sparkled.persistence.cache.impl.SongAudiosCache

interface CacheService {
    val gifs: GifsCache
    val settings: SettingsCache
    val songAudios: SongAudiosCache
    val allCaches: List<Cache<*>>
}


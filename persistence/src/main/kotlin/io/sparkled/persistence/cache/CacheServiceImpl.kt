package io.sparkled.persistence.cache

import io.sparkled.model.config.SparkledConfig
import io.sparkled.persistence.DbService
import io.sparkled.persistence.FileService
import io.sparkled.persistence.cache.impl.GifsCache
import io.sparkled.persistence.cache.impl.SettingsCache
import io.sparkled.persistence.cache.impl.SongAudiosCache
import jakarta.inject.Singleton
import kotlin.reflect.KClass
import kotlin.reflect.full.isSubclassOf
import kotlin.reflect.full.memberProperties

@Singleton
class CacheServiceImpl(
    config: SparkledConfig,
    db: DbService,
    fileService: FileService,
) : CacheService {

    final override val gifs = GifsCache(config)

    final override val settings = SettingsCache(db)

    final override val songAudios = SongAudiosCache(db, fileService)

    override val allCaches by lazy {
        this::class.memberProperties.filter {
            (it.returnType.classifier as? KClass<*>)?.isSubclassOf(Cache::class) ?: false
        }.map { it.getter.call(this) as Cache<*> }
    }
}

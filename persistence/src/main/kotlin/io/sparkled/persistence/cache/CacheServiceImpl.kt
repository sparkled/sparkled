package io.sparkled.persistence.cache

import com.madgag.gif.fmsware.GifDecoder
import io.sparkled.model.config.SparkledConfig
import io.sparkled.model.setting.SettingsCache
import io.sparkled.model.setting.SettingsConstants
import io.sparkled.persistence.DbService
import io.sparkled.persistence.getAll
import jakarta.inject.Singleton
import java.io.File
import java.util.*
import kotlin.reflect.full.memberProperties

@Singleton
class CacheServiceImpl(
    private val config: SparkledConfig,
    db: DbService,
) : CacheService {

    final override val gifs = Cache(
        name = "Gifs",
        fallback = linkedMapOf(),
    ) {
        val gifs = File("${config.dataFolderPath}/${config.gifFolderName}")
            .listFiles { _, name -> name.endsWith(".gif") } ?: emptyArray()

        val sortedGifs = gifs.sortedBy { it.name.lowercase(Locale.getDefault()) }

        val frames = sortedGifs.mapNotNull { file ->
            val decoder = GifDecoder()
            val status = decoder.read(file.inputStream())
            if (status != GifDecoder.STATUS_OK) {
                null
            } else {
                file.name to (0 until decoder.frameCount).map { decoder.getFrame(it) }
            }
        }

        frames.toMap(LinkedHashMap())
    }

    final override val settings = Cache(
        name = "Settings",
        fallback = SettingsCache(brightness = SettingsConstants.Brightness.MAX),
    ) {
        val brightness = db.getAll<SettingModel>().firstOrNull { it.code == SettingsConstants.Brightness.CODE }
        SettingsCache(brightness = brightness?.value?.toInt() ?: SettingsConstants.Brightness.MAX)
    }

    override val allCaches by lazy {
        this::class.memberProperties.filter {
            it.returnType.classifier == Cache::class
        }.map { it.getter.call(this) as Cache<*> }
    }
}

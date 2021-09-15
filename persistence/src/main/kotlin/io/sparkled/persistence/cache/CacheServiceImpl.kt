package io.sparkled.persistence.cache

import com.madgag.gif.fmsware.GifDecoder
import io.sparkled.model.config.SparkledConfig
import io.sparkled.model.entity.v2.SettingEntity
import io.sparkled.model.setting.SettingsCache
import io.sparkled.model.setting.SettingsConstants
import io.sparkled.persistence.DbService
import io.sparkled.persistence.getAll
import java.io.File
import java.util.*
import javax.inject.Singleton

@Singleton
class CacheServiceImpl(
    private val config: SparkledConfig,
    db: DbService,
) : CacheService {

    override val gifs = Cache(
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

    override val settings = Cache(
        name = "Settings",
        fallback = SettingsCache(brightness = SettingsConstants.Brightness.MAX),
    ) {
        val brightness = db.getAll<SettingEntity>().firstOrNull { it.code == SettingsConstants.Brightness.CODE }
        SettingsCache(brightness = brightness?.value?.toInt() ?: SettingsConstants.Brightness.MAX)
    }

    override val allCaches = listOf(settings)
}

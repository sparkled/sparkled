package io.sparkled.persistence.cache.impl

import com.madgag.gif.fmsware.GifDecoder
import io.sparkled.model.config.SparkledConfig
import io.sparkled.model.setting.SettingsCacheEntry
import io.sparkled.model.setting.SettingsConstants
import io.sparkled.persistence.DbService
import io.sparkled.persistence.cache.Cache
import java.awt.image.BufferedImage
import java.io.File
import java.time.Instant
import java.util.Locale

class GifsCache(
    private val config: SparkledConfig,
) : Cache<LinkedHashMap<String, List<BufferedImage>>>(
    name = "Gifs",
    fallback = linkedMapOf(),
) {
    override fun reload(lastLoadedAt: Instant?): LinkedHashMap<String, List<BufferedImage>> {
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

        return frames.toMap(LinkedHashMap())
    }
}

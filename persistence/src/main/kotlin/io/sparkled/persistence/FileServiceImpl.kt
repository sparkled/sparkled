package io.sparkled.persistence

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import io.sparkled.common.logging.getLogger
import io.sparkled.model.UniqueId
import io.sparkled.model.config.SparkledConfig
import io.sparkled.model.render.RenderedSequence
import jakarta.inject.Singleton
import java.io.File
import java.io.FileInputStream
import java.io.FileNotFoundException
import java.io.FileOutputStream
import java.util.zip.GZIPInputStream
import java.util.zip.GZIPOutputStream

@Singleton
class FileServiceImpl(
    private val objectMapper: ObjectMapper,
    private val config: SparkledConfig,
) : FileService {
    override fun init() {
        logger.info("Creating data folder structure.")
        File(config.dataFolderPath).mkdirs()
        config.dataFolders.forEach { File("${config.dataFolderPath}/$it").mkdirs() }
    }

    override fun readSongAudio(songId: UniqueId): ByteArray {
        val file = File(getSongAudioPath(songId))

        if (!file.exists()) {
            logger.error("Failed to find audio file ${file.name}.")
            throw FileNotFoundException()
        } else {
            return file.readBytes()
        }
    }

    override fun writeSongAudio(songId: UniqueId, audio: ByteArray) {
        File(getSongAudioPath(songId)).apply { createNewFile() }.writeBytes(audio)
    }

    override fun deleteSongAudio(songId: UniqueId) {
        File(getSongAudioPath(songId)).delete()
    }

    override fun readRender(sequenceId: UniqueId): RenderedSequence {
        val file = File(getRenderPath(sequenceId))

        return if (!file.exists()) {
            logger.error("Failed to find render file ${file.name}.")
            throw FileNotFoundException()
        } else {
            val stream = GZIPInputStream(FileInputStream(file))
            objectMapper.readValue(stream)
        }
    }

    override fun writeRender(sequenceId: UniqueId, render: RenderedSequence) {
        val file = File(getRenderPath(sequenceId)).apply { createNewFile() }
        GZIPOutputStream(FileOutputStream(file)).use {
            it.write(objectMapper.writeValueAsBytes(render))
        }
    }

    override fun deleteRender(sequenceId: UniqueId) {
        File(getRenderPath(sequenceId)).delete()
    }

    private fun getSongAudioPath(songId: UniqueId) = "${config.dataFolderPath}/${config.audioFolderName}/$songId.mp3"
    private fun getRenderPath(sequenceId: UniqueId) =
        "${config.dataFolderPath}/${config.renderFolderName}/$sequenceId.json.gz"

    companion object {
        private val logger = getLogger<FileServiceImpl>()
    }
}

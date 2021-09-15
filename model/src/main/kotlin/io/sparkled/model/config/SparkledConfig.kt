package io.sparkled.model.config

import io.micronaut.context.annotation.Value
import javax.inject.Singleton

@Singleton
data class SparkledConfig(

    /**
     * The path of the folder containing Sparkled data, such as plugins.
     */
    @Value("\${sparkled.data-folder-path:./sparkledData}")
    val dataFolderPath: String,

    /**
     * The name of the folder containing song audio.
     */
    @Value("\${sparkled.audio-folder-name:audio}")
    val audioFolderName: String,

    /**
     * The name of the folder containing plugins.
     */
    @Value("\${sparkled.gif-folder-name:gifs}")
    val gifFolderName: String,

    /**
     * The name of the folder containing plugins.
     */
    @Value("\${sparkled.plugin-folder-name:plugins}")
    val pluginFolderName: String,

    /**
     * The name of the folder containing sequence renders.
     */
    @Value("\${sparkled.render-folder-name:renders}")
    val renderFolderName: String,
) {
    val dataFolders = listOf(audioFolderName, gifFolderName, pluginFolderName, renderFolderName)
}

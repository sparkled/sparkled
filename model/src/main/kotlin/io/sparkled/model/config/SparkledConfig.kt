package io.sparkled.model.config

import io.micronaut.context.annotation.Value
import jakarta.inject.Singleton

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

    /**
     * The port that Sparkled clients connect to in order to receive data.
     */
    @Value("\${sparkled.client-udp-port:2812}")
    val clientUdpPort: Int,

    /**
     * The size, in bytes, of the UDP buffer used to receive data from clients.
     */
    @Value("\${sparkled.udp-buffer-size:25000000}")
    val udpReceiveBufferSize: Int,

    /**
     * The size, in bytes, of the UDP buffer used to send data down to clients.
     */
    @Value("\${sparkled.udp-buffer-size:25000000}")
    val udpSendBufferSize: Int,
) {
    val dataFolders by lazy {
        listOf(audioFolderName, gifFolderName, pluginFolderName, renderFolderName)
    }
}

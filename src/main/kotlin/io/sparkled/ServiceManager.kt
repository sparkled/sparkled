package io.sparkled

import io.micronaut.context.ApplicationContext
import io.micronaut.runtime.event.ApplicationShutdownEvent
import io.micronaut.runtime.event.ApplicationStartupEvent
import io.micronaut.runtime.event.annotation.EventListener
import io.micronaut.transaction.annotation.Transactional
import io.sparkled.api.websocket.WebSocketServer
import io.sparkled.common.logging.getLogger
import io.sparkled.model.config.SparkledConfig
import io.sparkled.persistence.FileService
import io.sparkled.persistence.cache.CacheService
import io.sparkled.renderer.SparkledPluginManager
import io.sparkled.scheduler.SchedulerService
import io.sparkled.udpserver.LedDataStreamer
import io.sparkled.udpserver.UdpServer
import jakarta.inject.Singleton
import java.net.DatagramSocket

@Singleton
class ServiceManager(
    private val applicationContext: ApplicationContext,
    private val cache: CacheService,
    private val config: SparkledConfig,
    private val fileService: FileService,
    private val ledDataStreamer: LedDataStreamer,
    private val pluginManager: SparkledPluginManager,
    private val schedulerService: SchedulerService,
    private val udpServer: UdpServer,
    private val webSocketServer: WebSocketServer,
) {

    @EventListener
    @Transactional
    fun onStartup(event: ApplicationStartupEvent) {
        fileService.init()

        // Pre-warm caches.
        cache.settings.get()
        cache.gifs.get()

        pluginManager.reloadPlugins()
        schedulerService.start()

        val socket = buildSocket()
        udpServer.start(socket)
        ledDataStreamer.start(socket)
        webSocketServer.start()

        when {
            "e2eTest" in applicationContext.environment.activeNames -> {
                logger.info("Sparkled server running in e2eTest mode.")
            }

            else -> {
                logger.info("Sparkled server is running.")
            }
        }
    }

    private fun buildSocket(): DatagramSocket {
        val socket = DatagramSocket(config.clientUdpPort)

        try {
            socket.sendBufferSize = config.udpSendBufferSize
            socket.receiveBufferSize = config.udpReceiveBufferSize
        } catch (e: Exception) {
            logger.error("Failed to set socket buffer size.", e)
        }

        logger.info("Socket send buffer size: ${socket.sendBufferSize} bytes.")
        logger.info("Socket receive buffer size: ${socket.receiveBufferSize} bytes.")
        return socket
    }

    @EventListener
    fun onShutdown(event: ApplicationShutdownEvent) {
        schedulerService.stop()
        udpServer.stop()
        ledDataStreamer.stop()
    }

    companion object {
        private val logger = getLogger<ServiceManager>()
    }
}

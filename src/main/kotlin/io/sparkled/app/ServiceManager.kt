package io.sparkled.app

import io.micronaut.context.ApplicationContext
import io.micronaut.runtime.event.annotation.EventListener
import io.micronaut.runtime.server.event.ServerShutdownEvent
import io.micronaut.runtime.server.event.ServerStartupEvent
import io.sparkled.persistence.DbService
import io.sparkled.persistence.FileService
import io.sparkled.renderer.SparkledPluginManager
import io.sparkled.scheduler.SchedulerService
import io.sparkled.udpserver.LedDataStreamer
import io.sparkled.udpserver.UdpServer
import jakarta.inject.Singleton
import org.slf4j.LoggerFactory
import org.springframework.transaction.annotation.Transactional
import java.net.DatagramSocket

@Singleton
class ServiceManager(
    private val applicationContext: ApplicationContext,
    private val schedulerService: SchedulerService,
    private val udpServer: UdpServer,
    private val ledDataStreamer: LedDataStreamer,
    private val pluginManager: SparkledPluginManager,
    private val db: DbService,
    private val file: FileService,
) {

    @EventListener
    @Transactional(readOnly = true)
    fun onStartup(event: ServerStartupEvent) {
        db.init()
        file.init()
        pluginManager.reloadPlugins()
        schedulerService.start()

        val socket = buildSocket()
        udpServer.start(socket)
        ledDataStreamer.start(socket)

        when {
            applicationContext.environment.activeNames.contains("e2eTest") -> {
                logger.info("Server running in e2eTest mode.")
            }
            else -> logger.info("Sparkled server is running.")
        }
    }

    private fun buildSocket(): DatagramSocket {
        // TODO make configurable.
        val socket = DatagramSocket(2812)

        try {
            // TODO make configurable.
            socket.sendBufferSize = UDP_BUFFER_SIZE
            socket.receiveBufferSize = UDP_BUFFER_SIZE
        } catch (e: Exception) {
            logger.error("Failed to set socket buffer size.", e)
        }

        logger.info("Socket send buffer size: ${socket.sendBufferSize} bytes.")
        logger.info("Socket receive buffer size: ${socket.receiveBufferSize} bytes.")
        return socket
    }

    @EventListener
    fun onShutdown(event: ServerShutdownEvent) {
        schedulerService.stop()
        udpServer.stop()
        ledDataStreamer.stop()
    }

    companion object {
        private val logger = LoggerFactory.getLogger(ServiceManager::class.java)
        private const val UDP_BUFFER_SIZE = 25 * 1024 * 1024 // 25MB
    }
}

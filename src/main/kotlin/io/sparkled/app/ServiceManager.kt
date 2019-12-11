package io.sparkled.app

import io.micronaut.runtime.event.annotation.EventListener
import io.micronaut.runtime.server.event.ServerShutdownEvent
import io.micronaut.runtime.server.event.ServerStartupEvent
import io.micronaut.spring.tx.annotation.Transactional
import io.sparkled.persistence.setting.SettingPersistenceService
import io.sparkled.scheduler.SchedulerService
import io.sparkled.udpserver.LedDataStreamer
import io.sparkled.udpserver.UdpServer
import org.slf4j.LoggerFactory
import java.net.DatagramSocket
import javax.inject.Singleton

@Singleton
open class ServiceManager(
    private val settingPersistenceService: SettingPersistenceService,
    private val schedulerService: SchedulerService,
    private val udpServer: UdpServer,
    private val ledDataStreamer: LedDataStreamer
) {

    @EventListener
    @Transactional(readOnly = true)
    open fun onStartup(event: ServerStartupEvent) {
        settingPersistenceService.reloadSettingsCache()
        schedulerService.start()

        val socket = buildSocket()
        udpServer.start(socket)
        ledDataStreamer.start(socket)

        println("Sparkled server is running.")
    }

    private fun buildSocket(): DatagramSocket {
        val socket = DatagramSocket(2812)

        try {
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
    open fun onShutdown(event: ServerShutdownEvent) {
        schedulerService.stop()
        udpServer.stop()
        ledDataStreamer.stop()
    }

    companion object {
        private val logger = LoggerFactory.getLogger(ServiceManager::class.java)
        private const val UDP_BUFFER_SIZE = 25 * 1024 * 1024 // 25MB
    }
}

package io.sparkled.app

import io.micronaut.runtime.event.annotation.EventListener
import io.micronaut.runtime.server.event.ServerShutdownEvent
import io.micronaut.runtime.server.event.ServerStartupEvent
import io.micronaut.spring.tx.annotation.Transactional
import io.sparkled.scheduler.SchedulerService
import io.sparkled.udpserver.UdpServer
import javax.inject.Singleton

@Singleton
open class ServiceManager(
    private val schedulerService: SchedulerService,
    private val udpServer: UdpServer
) {

    @EventListener
    @Transactional(readOnly = true)
    open fun onStartup(event: ServerStartupEvent) {
        schedulerService.start()
        udpServer.start(2812)
    }

    @EventListener
    open fun onStartup(event: ServerShutdownEvent) {
        schedulerService.stop()
        udpServer.stop()
    }
}

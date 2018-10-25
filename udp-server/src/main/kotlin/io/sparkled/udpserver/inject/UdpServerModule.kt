package io.sparkled.udpserver.inject

import com.google.inject.AbstractModule
import io.sparkled.udpserver.RequestHandler
import io.sparkled.udpserver.UdpServer
import io.sparkled.udpserver.impl.RequestHandlerImpl
import io.sparkled.udpserver.impl.UdpServerImpl
import org.slf4j.LoggerFactory

class UdpServerModule : AbstractModule() {

    override fun configure() {
        logger.info("Configuring Guice module.")

        bind(UdpServer::class.java).to(UdpServerImpl::class.java).asEagerSingleton()
        bind(RequestHandler::class.java).to(RequestHandlerImpl::class.java).asEagerSingleton()
    }

    companion object {
        private val logger = LoggerFactory.getLogger(UdpServerModule::class.java)
    }
}

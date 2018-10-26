package io.sparkled.rest.jetty

import com.google.inject.Injector
import org.glassfish.hk2.api.ServiceLocator
import org.glassfish.jersey.server.ResourceConfig
import org.jvnet.hk2.guice.bridge.api.GuiceBridge
import org.jvnet.hk2.guice.bridge.api.GuiceIntoHK2Bridge
import org.slf4j.LoggerFactory
import javax.inject.Inject

/**
 * Facilitates Guice injection of Jersey REST service classes.
 */
class JerseyResourceConfig @Inject
constructor(serviceLocator: ServiceLocator) : ResourceConfig() {

    init {
        logger.info("Creating Guice bridge.")
        createBiDirectionalGuiceBridge(serviceLocator)
        logger.info("Created Guice bridge successfully.")
    }

    private fun createBiDirectionalGuiceBridge(serviceLocator: ServiceLocator) {
        GuiceBridge.getGuiceBridge().initializeGuiceBridge(serviceLocator)
        val g2h = serviceLocator.getService(GuiceIntoHK2Bridge::class.java)
        g2h.bridgeGuiceInjector(injector)
    }

    companion object {

        private val logger = LoggerFactory.getLogger(JerseyResourceConfig::class.java)
        private var injector: Injector? = null

        fun setInjector(injector: Injector) {
            JerseyResourceConfig.injector = injector
        }
    }
}
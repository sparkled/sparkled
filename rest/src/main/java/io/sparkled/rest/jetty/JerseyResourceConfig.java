package io.sparkled.rest.jetty;

import com.google.inject.Injector;
import org.glassfish.hk2.api.ServiceLocator;
import org.glassfish.jersey.server.ResourceConfig;
import org.jvnet.hk2.guice.bridge.api.GuiceBridge;
import org.jvnet.hk2.guice.bridge.api.GuiceIntoHK2Bridge;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;

/**
 * Facilitates Guice injection of Jersey REST service classes.
 */
public class JerseyResourceConfig extends ResourceConfig {

    private static Logger logger = LoggerFactory.getLogger(JerseyResourceConfig.class);
    private static Injector injector;

    public static void setInjector(Injector injector) {
        JerseyResourceConfig.injector = injector;
    }

    @Inject
    public JerseyResourceConfig(ServiceLocator serviceLocator) {
        logger.info("Creating Guice bridge.");
        createBiDirectionalGuiceBridge(serviceLocator);
        logger.info("Created Guice bridge successfully.");
    }

    private void createBiDirectionalGuiceBridge(ServiceLocator serviceLocator) {
        GuiceBridge.getGuiceBridge().initializeGuiceBridge(serviceLocator);
        GuiceIntoHK2Bridge g2h = serviceLocator.getService(GuiceIntoHK2Bridge.class);
        g2h.bridgeGuiceInjector(injector);
    }
}
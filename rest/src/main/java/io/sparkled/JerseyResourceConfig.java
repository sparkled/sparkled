package io.sparkled;

import com.google.inject.Injector;
import org.glassfish.hk2.api.ServiceLocator;
import org.glassfish.jersey.server.ResourceConfig;
import org.jvnet.hk2.guice.bridge.api.GuiceBridge;
import org.jvnet.hk2.guice.bridge.api.GuiceIntoHK2Bridge;

import javax.inject.Inject;
import java.util.logging.Logger;

/**
 * Facilitates Guice injection of Jersey REST service classes.
 */
public class JerseyResourceConfig extends ResourceConfig {

    private static Logger logger = Logger.getLogger(JerseyResourceConfig.class.getName());
    private static Injector injector;

    static void setInjector(Injector injector) {
        JerseyResourceConfig.injector = injector;
    }

    @Inject
    public JerseyResourceConfig(ServiceLocator serviceLocator) {
        logger.info("Creating Guice bridge");
        createBiDirectionalGuiceBridge(serviceLocator);
        logger.info("Created Guice bridge successfully.");
    }

    private void createBiDirectionalGuiceBridge(ServiceLocator serviceLocator) {
        GuiceBridge.getGuiceBridge().initializeGuiceBridge(serviceLocator);
        GuiceIntoHK2Bridge g2h = serviceLocator.getService(GuiceIntoHK2Bridge.class);
        g2h.bridgeGuiceInjector(injector);
    }
}
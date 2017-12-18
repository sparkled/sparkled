package io.sparkled;

import com.google.inject.Injector;
import org.glassfish.hk2.api.ServiceLocator;
import org.glassfish.jersey.server.ResourceConfig;
import org.jvnet.hk2.guice.bridge.api.GuiceBridge;
import org.jvnet.hk2.guice.bridge.api.GuiceIntoHK2Bridge;

import javax.inject.Inject;
import java.util.logging.Logger;

public class JerseyResourceConfig extends ResourceConfig {
    private static Logger logger = Logger.getLogger(JerseyResourceConfig.class.getName());

    @Inject
    public JerseyResourceConfig(ServiceLocator serviceLocator) {
        logger.info("Creating Guice bridge");
        createBiDirectionalGuiceBridge(serviceLocator);
    }

    private Injector createBiDirectionalGuiceBridge(ServiceLocator serviceLocator) {
        GuiceBridge.getGuiceBridge().initializeGuiceBridge(serviceLocator);
        Injector injector = RestApiServerImpl.injector;
        GuiceIntoHK2Bridge g2h = serviceLocator.getService(GuiceIntoHK2Bridge.class);
        g2h.bridgeGuiceInjector(injector);

        return injector;
    }
}
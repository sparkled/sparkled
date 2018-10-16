package io.sparkled.udpserver.inject;

import com.google.inject.AbstractModule;
import io.sparkled.udpserver.RequestHandler;
import io.sparkled.udpserver.RequestHandlerImpl;
import io.sparkled.udpserver.UdpServer;
import io.sparkled.udpserver.UdpServerImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class UdpServerModule extends AbstractModule {

    private static final Logger logger = LoggerFactory.getLogger(UdpServerModule.class);

    @Override
    protected void configure() {
        logger.info("Configuring Guice module.");

        bind(UdpServer.class).to(UdpServerImpl.class).asEagerSingleton();
        bind(RequestHandler.class).to(RequestHandlerImpl.class).asEagerSingleton();
    }
}

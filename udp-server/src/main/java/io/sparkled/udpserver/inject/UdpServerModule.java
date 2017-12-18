package io.sparkled.udpserver.inject;

import com.google.inject.AbstractModule;
import io.sparkled.udpserver.RequestHandler;
import io.sparkled.udpserver.RequestHandlerImpl;
import io.sparkled.udpserver.UdpServer;
import io.sparkled.udpserver.UdpServerImpl;

public class UdpServerModule extends AbstractModule {

    @Override
    protected void configure() {
        bind(UdpServer.class).to(UdpServerImpl.class).asEagerSingleton();
        bind(RequestHandler.class).to(RequestHandlerImpl.class).asEagerSingleton();
    }
}

package net.chrisparton.sparkled.udpserver.inject;

import com.google.inject.AbstractModule;
import net.chrisparton.sparkled.udpserver.RequestHandler;
import net.chrisparton.sparkled.udpserver.RequestHandlerImpl;
import net.chrisparton.sparkled.udpserver.UdpServer;
import net.chrisparton.sparkled.udpserver.UdpServerImpl;

public class UdpServerModule extends AbstractModule {

    @Override
    protected void configure() {
        bind(UdpServer.class).to(UdpServerImpl.class).asEagerSingleton();
        bind(RequestHandler.class).to(RequestHandlerImpl.class).asEagerSingleton();
    }
}

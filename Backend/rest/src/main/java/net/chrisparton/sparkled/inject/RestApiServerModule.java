package net.chrisparton.sparkled.inject;

import com.google.inject.AbstractModule;
import net.chrisparton.sparkled.RestApiServer;
import net.chrisparton.sparkled.RestApiServerImpl;

public class RestApiServerModule extends AbstractModule {

    @Override
    protected void configure() {
        bind(RestApiServer.class).to(RestApiServerImpl.class).asEagerSingleton();
    }
}

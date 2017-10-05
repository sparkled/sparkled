package net.chrisparton.sparkled.inject;

import com.google.inject.AbstractModule;
import net.chrisparton.sparkled.RestApiServer;
import net.chrisparton.sparkled.RestApiServerImpl;
import net.chrisparton.sparkled.viewmodel.converter.ScheduledSongViewModelConverter;
import net.chrisparton.sparkled.viewmodel.converter.ScheduledSongViewModelConverterImpl;

public class RestApiServerModule extends AbstractModule {

    @Override
    protected void configure() {
        bind(RestApiServer.class).to(RestApiServerImpl.class).asEagerSingleton();
        bind(ScheduledSongViewModelConverter.class).to(ScheduledSongViewModelConverterImpl.class).asEagerSingleton();
    }
}

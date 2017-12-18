package io.sparkled.inject;

import com.google.inject.AbstractModule;
import io.sparkled.RestApiServer;
import io.sparkled.RestApiServerImpl;
import io.sparkled.viewmodel.converter.ScheduledSongViewModelConverter;
import io.sparkled.viewmodel.converter.ScheduledSongViewModelConverterImpl;

public class RestApiServerModule extends AbstractModule {

    @Override
    protected void configure() {
        bind(RestApiServer.class).to(RestApiServerImpl.class).asEagerSingleton();
        bind(ScheduledSongViewModelConverter.class).to(ScheduledSongViewModelConverterImpl.class).asEagerSingleton();
    }
}

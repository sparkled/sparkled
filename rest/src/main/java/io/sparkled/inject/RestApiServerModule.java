package io.sparkled.inject;

import com.google.inject.AbstractModule;
import io.sparkled.RestApiServer;
import io.sparkled.RestApiServerImpl;
import io.sparkled.viewmodel.converter.ScheduledSequenceViewModelConverter;
import io.sparkled.viewmodel.converter.ScheduledSequenceViewModelConverterImpl;

public class RestApiServerModule extends AbstractModule {

    @Override
    protected void configure() {
        bind(RestApiServer.class).to(RestApiServerImpl.class).asEagerSingleton();
        bind(ScheduledSequenceViewModelConverter.class).to(ScheduledSequenceViewModelConverterImpl.class).asEagerSingleton();
    }
}

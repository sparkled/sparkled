package io.sparkled.inject;

import com.google.inject.AbstractModule;
import io.sparkled.RestApiServer;
import io.sparkled.RestApiServerImpl;
import io.sparkled.viewmodel.ScheduledSequenceViewModelConverter;
import io.sparkled.viewmodel.ScheduledSequenceViewModelConverterImpl;
import io.sparkled.viewmodel.sequence.SequenceViewModelConverter;
import io.sparkled.viewmodel.sequence.SequenceViewModelConverterImpl;
import io.sparkled.viewmodel.sequence.channel.SequenceChannelViewModelConverter;
import io.sparkled.viewmodel.sequence.channel.SequenceChannelViewModelConverterImpl;

public class RestApiServerModule extends AbstractModule {

    @Override
    protected void configure() {
        bind(RestApiServer.class).to(RestApiServerImpl.class).asEagerSingleton();
        bind(SequenceViewModelConverter.class).to(SequenceViewModelConverterImpl.class).asEagerSingleton();
        bind(SequenceChannelViewModelConverter.class).to(SequenceChannelViewModelConverterImpl.class).asEagerSingleton();
        bind(ScheduledSequenceViewModelConverter.class).to(ScheduledSequenceViewModelConverterImpl.class).asEagerSingleton();
    }
}

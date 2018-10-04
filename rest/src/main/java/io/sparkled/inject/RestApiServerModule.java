package io.sparkled.inject;

import com.google.inject.AbstractModule;
import io.sparkled.RestApiServer;
import io.sparkled.RestApiServerImpl;
import io.sparkled.viewmodel.playlist.PlaylistViewModelConverter;
import io.sparkled.viewmodel.playlist.PlaylistViewModelConverterImpl;
import io.sparkled.viewmodel.playlist.sequence.PlaylistSequenceViewModelConverter;
import io.sparkled.viewmodel.playlist.sequence.PlaylistSequenceViewModelConverterImpl;
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
        bind(PlaylistViewModelConverter.class).to(PlaylistViewModelConverterImpl.class).asEagerSingleton();
        bind(PlaylistSequenceViewModelConverter.class).to(PlaylistSequenceViewModelConverterImpl.class).asEagerSingleton();
    }
}

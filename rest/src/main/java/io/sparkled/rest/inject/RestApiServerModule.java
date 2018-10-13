package io.sparkled.rest.inject;

import com.google.inject.AbstractModule;
import io.sparkled.rest.RestApiServer;
import io.sparkled.rest.RestApiServerImpl;
import io.sparkled.viewmodel.playlist.PlaylistViewModelConverter;
import io.sparkled.viewmodel.playlist.PlaylistViewModelConverterImpl;
import io.sparkled.viewmodel.playlist.search.PlaylistSearchViewModelConverter;
import io.sparkled.viewmodel.playlist.search.PlaylistSearchViewModelConverterImpl;
import io.sparkled.viewmodel.playlist.sequence.PlaylistSequenceViewModelConverter;
import io.sparkled.viewmodel.playlist.sequence.PlaylistSequenceViewModelConverterImpl;
import io.sparkled.viewmodel.sequence.SequenceViewModelConverter;
import io.sparkled.viewmodel.sequence.SequenceViewModelConverterImpl;
import io.sparkled.viewmodel.sequence.channel.SequenceChannelViewModelConverter;
import io.sparkled.viewmodel.sequence.channel.SequenceChannelViewModelConverterImpl;
import io.sparkled.viewmodel.sequence.search.SequenceSearchViewModelConverter;
import io.sparkled.viewmodel.sequence.search.SequenceSearchViewModelConverterImpl;
import io.sparkled.viewmodel.song.SongViewModelConverter;
import io.sparkled.viewmodel.song.SongViewModelConverterImpl;
import io.sparkled.viewmodel.stage.StageViewModelConverter;
import io.sparkled.viewmodel.stage.StageViewModelConverterImpl;
import io.sparkled.viewmodel.stage.prop.StagePropViewModelConverter;
import io.sparkled.viewmodel.stage.prop.StagePropViewModelConverterImpl;
import io.sparkled.viewmodel.stage.search.StageSearchViewModelConverter;
import io.sparkled.viewmodel.stage.search.StageSearchViewModelConverterImpl;

public class RestApiServerModule extends AbstractModule {

    @Override
    protected void configure() {
        bind(RestApiServer.class).to(RestApiServerImpl.class).asEagerSingleton();

        bind(SongViewModelConverter.class).to(SongViewModelConverterImpl.class).asEagerSingleton();

        bind(StageViewModelConverter.class).to(StageViewModelConverterImpl.class).asEagerSingleton();
        bind(StageSearchViewModelConverter.class).to(StageSearchViewModelConverterImpl.class).asEagerSingleton();
        bind(StagePropViewModelConverter.class).to(StagePropViewModelConverterImpl.class).asEagerSingleton();

        bind(SequenceViewModelConverter.class).to(SequenceViewModelConverterImpl.class).asEagerSingleton();
        bind(SequenceSearchViewModelConverter.class).to(SequenceSearchViewModelConverterImpl.class).asEagerSingleton();
        bind(SequenceChannelViewModelConverter.class).to(SequenceChannelViewModelConverterImpl.class).asEagerSingleton();

        bind(PlaylistViewModelConverter.class).to(PlaylistViewModelConverterImpl.class).asEagerSingleton();
        bind(PlaylistSearchViewModelConverter.class).to(PlaylistSearchViewModelConverterImpl.class).asEagerSingleton();
        bind(PlaylistSequenceViewModelConverter.class).to(PlaylistSequenceViewModelConverterImpl.class).asEagerSingleton();
    }
}

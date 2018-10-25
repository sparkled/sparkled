package io.sparkled.rest.inject

import com.google.inject.AbstractModule
import io.sparkled.rest.RestApiServer
import io.sparkled.rest.RestApiServerImpl
import io.sparkled.viewmodel.playlist.PlaylistViewModelConverter
import io.sparkled.viewmodel.playlist.PlaylistViewModelConverterImpl
import io.sparkled.viewmodel.playlist.search.PlaylistSearchViewModelConverter
import io.sparkled.viewmodel.playlist.search.PlaylistSearchViewModelConverterImpl
import io.sparkled.viewmodel.playlist.sequence.PlaylistSequenceViewModelConverter
import io.sparkled.viewmodel.playlist.sequence.PlaylistSequenceViewModelConverterImpl
import io.sparkled.viewmodel.sequence.SequenceViewModelConverter
import io.sparkled.viewmodel.sequence.SequenceViewModelConverterImpl
import io.sparkled.viewmodel.sequence.channel.SequenceChannelViewModelConverter
import io.sparkled.viewmodel.sequence.channel.SequenceChannelViewModelConverterImpl
import io.sparkled.viewmodel.sequence.search.SequenceSearchViewModelConverter
import io.sparkled.viewmodel.sequence.search.SequenceSearchViewModelConverterImpl
import io.sparkled.viewmodel.song.SongViewModelConverter
import io.sparkled.viewmodel.song.SongViewModelConverterImpl
import io.sparkled.viewmodel.stage.StageViewModelConverter
import io.sparkled.viewmodel.stage.StageViewModelConverterImpl
import io.sparkled.viewmodel.stage.prop.StagePropViewModelConverter
import io.sparkled.viewmodel.stage.prop.StagePropViewModelConverterImpl
import io.sparkled.viewmodel.stage.search.StageSearchViewModelConverter
import io.sparkled.viewmodel.stage.search.StageSearchViewModelConverterImpl
import org.slf4j.Logger
import org.slf4j.LoggerFactory

class RestApiServerModule : AbstractModule() {

    @Override
    protected fun configure() {
        logger.info("Configuring Guice module.")

        bind(RestApiServer::class.java).to(RestApiServerImpl::class.java).asEagerSingleton()

        bind(SongViewModelConverter::class.java).to(SongViewModelConverterImpl::class.java).asEagerSingleton()

        bind(StageViewModelConverter::class.java).to(StageViewModelConverterImpl::class.java).asEagerSingleton()
        bind(StageSearchViewModelConverter::class.java).to(StageSearchViewModelConverterImpl::class.java).asEagerSingleton()
        bind(StagePropViewModelConverter::class.java).to(StagePropViewModelConverterImpl::class.java).asEagerSingleton()

        bind(SequenceViewModelConverter::class.java).to(SequenceViewModelConverterImpl::class.java).asEagerSingleton()
        bind(SequenceSearchViewModelConverter::class.java).to(SequenceSearchViewModelConverterImpl::class.java).asEagerSingleton()
        bind(SequenceChannelViewModelConverter::class.java).to(SequenceChannelViewModelConverterImpl::class.java).asEagerSingleton()

        bind(PlaylistViewModelConverter::class.java).to(PlaylistViewModelConverterImpl::class.java).asEagerSingleton()
        bind(PlaylistSearchViewModelConverter::class.java).to(PlaylistSearchViewModelConverterImpl::class.java).asEagerSingleton()
        bind(PlaylistSequenceViewModelConverter::class.java).to(PlaylistSequenceViewModelConverterImpl::class.java).asEagerSingleton()
    }

    companion object {

        private val logger = LoggerFactory.getLogger(RestApiServerModule::class.java)
    }
}

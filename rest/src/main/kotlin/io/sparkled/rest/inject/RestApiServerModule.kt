package io.sparkled.rest.inject

import com.google.inject.AbstractModule
import com.google.inject.Singleton
import io.sparkled.rest.RestApiServer
import io.sparkled.rest.RestApiServerImpl
import io.sparkled.rest.service.playlist.PlaylistRestServiceHandler
import io.sparkled.viewmodel.playlist.PlaylistViewModelConverter
import io.sparkled.viewmodel.playlist.PlaylistViewModelConverterImpl
import io.sparkled.viewmodel.playlist.search.PlaylistSearchViewModelConverter
import io.sparkled.viewmodel.playlist.search.PlaylistSearchViewModelConverterImpl
import io.sparkled.viewmodel.playlist.sequence.PlaylistSequenceViewModelConverter
import io.sparkled.viewmodel.playlist.sequence.PlaylistSequenceViewModelConverterImpl
import io.sparkled.viewmodel.scheduledjob.ScheduledJobViewModelConverter
import io.sparkled.viewmodel.scheduledjob.ScheduledJobViewModelConverterImpl
import io.sparkled.viewmodel.scheduledjob.search.ScheduledJobSearchViewModelConverter
import io.sparkled.viewmodel.scheduledjob.search.ScheduledJobSearchViewModelConverterImpl
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
import org.slf4j.LoggerFactory

class RestApiServerModule : AbstractModule() {

    override fun configure() {
        logger.info("Configuring Guice module.")

        bindTo(RestApiServer::class.java, RestApiServerImpl::class.java)
        bind(PlaylistRestServiceHandler::class.java).`in`(Singleton::class.java)

        bindTo(SongViewModelConverter::class.java, SongViewModelConverterImpl::class.java)

        bindTo(StageViewModelConverter::class.java, StageViewModelConverterImpl::class.java)
        bindTo(StageSearchViewModelConverter::class.java, StageSearchViewModelConverterImpl::class.java)
        bindTo(StagePropViewModelConverter::class.java, StagePropViewModelConverterImpl::class.java)

        bindTo(SequenceViewModelConverter::class.java, SequenceViewModelConverterImpl::class.java)
        bindTo(SequenceSearchViewModelConverter::class.java, SequenceSearchViewModelConverterImpl::class.java)
        bindTo(SequenceChannelViewModelConverter::class.java, SequenceChannelViewModelConverterImpl::class.java)

        bindTo(PlaylistViewModelConverter::class.java, PlaylistViewModelConverterImpl::class.java)
        bindTo(PlaylistSearchViewModelConverter::class.java, PlaylistSearchViewModelConverterImpl::class.java)
        bindTo(PlaylistSequenceViewModelConverter::class.java, PlaylistSequenceViewModelConverterImpl::class.java)

        bindTo(ScheduledJobViewModelConverter::class.java, ScheduledJobViewModelConverterImpl::class.java)
        bindTo(ScheduledJobSearchViewModelConverter::class.java, ScheduledJobSearchViewModelConverterImpl::class.java)
    }

    private fun <T> bindTo(a: Class<T>, b: Class<out T>) {
        bind(a).to(b).asEagerSingleton()
    }

    companion object {
        private val logger = LoggerFactory.getLogger(RestApiServerModule::class.java)
    }
}

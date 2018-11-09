package io.sparkled.music.inject

import com.google.inject.AbstractModule
import io.sparkled.music.MusicPlayerService
import io.sparkled.music.PlaybackService
import io.sparkled.music.PlaybackStateService
import io.sparkled.music.impl.MusicPlayerServiceImpl
import io.sparkled.music.impl.PlaybackServiceImpl
import org.slf4j.LoggerFactory
import javax.inject.Singleton

class MusicPlayerModule : AbstractModule() {

    override fun configure() {
        logger.info("Configuring Guice module.")

        // PlaybackServiceImpl is bound to two interfaces. This scope binding ensures they both get the same singleton instance.
        bind<PlaybackServiceImpl>(PlaybackServiceImpl::class.java).`in`(Singleton::class.java)
        bind<PlaybackService>(PlaybackService::class.java).to(PlaybackServiceImpl::class.java).asEagerSingleton()
        bind<PlaybackStateService>(PlaybackStateService::class.java).to(PlaybackServiceImpl::class.java)
            .asEagerSingleton()

        bind<MusicPlayerService>(MusicPlayerService::class.java).to(MusicPlayerServiceImpl::class.java)
            .asEagerSingleton()
    }

    companion object {
        private val logger = LoggerFactory.getLogger(MusicPlayerModule::class.java)
    }
}

package io.sparkled.music.inject;

import com.google.inject.AbstractModule;
import io.sparkled.music.MusicPlayerService;
import io.sparkled.music.PlaybackService;
import io.sparkled.music.PlaybackStateService;
import io.sparkled.music.impl.MusicPlayerServiceImpl;
import io.sparkled.music.impl.PlaybackServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Singleton;

public class MusicPlayerModule extends AbstractModule {

    private static final Logger logger = LoggerFactory.getLogger(MusicPlayerModule.class);

    @Override
    protected void configure() {
        logger.info("Configuring Guice module.");

        // PlaybackServiceImpl is bound to two interfaces. This scope binding ensures they both get the same singleton instance.
        bind(PlaybackServiceImpl.class).in(Singleton.class);
        bind(PlaybackService.class).to(PlaybackServiceImpl.class).asEagerSingleton();
        bind(PlaybackStateService.class).to(PlaybackServiceImpl.class).asEagerSingleton();

        bind(MusicPlayerService.class).to(MusicPlayerServiceImpl.class).asEagerSingleton();
    }
}

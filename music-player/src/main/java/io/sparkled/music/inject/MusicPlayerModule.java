package io.sparkled.music.inject;

import com.google.inject.AbstractModule;
import io.sparkled.music.MusicPlayerService;
import io.sparkled.music.impl.MusicPlayerServiceImpl;

public class MusicPlayerModule extends AbstractModule {

    @Override
    protected void configure() {
        bind(MusicPlayerService.class).to(MusicPlayerServiceImpl.class).asEagerSingleton();
    }
}

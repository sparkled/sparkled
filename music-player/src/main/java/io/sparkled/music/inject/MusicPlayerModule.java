package io.sparkled.music.inject;

import com.google.inject.AbstractModule;
import io.sparkled.music.PlaylistService;
import io.sparkled.music.PlaylistServiceImpl;

public class MusicPlayerModule extends AbstractModule {

    @Override
    protected void configure() {
        bind(PlaylistService.class).to(PlaylistServiceImpl.class);
    }
}

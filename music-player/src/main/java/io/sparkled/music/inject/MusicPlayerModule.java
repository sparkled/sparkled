package io.sparkled.music.inject;

import com.google.inject.AbstractModule;
import io.sparkled.music.SongSchedulerServiceImpl;
import io.sparkled.music.SongPlayerService;
import io.sparkled.music.SongPlayerServiceImpl;
import io.sparkled.music.SongSchedulerService;

public class MusicPlayerModule extends AbstractModule {

    @Override
    protected void configure() {
        bind(SongSchedulerService.class).to(SongSchedulerServiceImpl.class);
        bind(SongPlayerService.class).to(SongPlayerServiceImpl.class);
    }
}

package net.chrisparton.sparkled.music.inject;

import com.google.inject.AbstractModule;
import net.chrisparton.sparkled.music.SongPlayerService;
import net.chrisparton.sparkled.music.SongPlayerServiceImpl;
import net.chrisparton.sparkled.music.SongSchedulerService;
import net.chrisparton.sparkled.music.SongSchedulerServiceImpl;

public class MusicPlayerModule extends AbstractModule {

    @Override
    protected void configure() {
        bind(SongSchedulerService.class).to(SongSchedulerServiceImpl.class);
        bind(SongPlayerService.class).to(SongPlayerServiceImpl.class);
    }
}

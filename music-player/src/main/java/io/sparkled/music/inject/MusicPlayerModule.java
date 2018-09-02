package io.sparkled.music.inject;

import com.google.inject.AbstractModule;
import io.sparkled.music.SequenceSchedulerServiceImpl;
import io.sparkled.music.SequencePlayerService;
import io.sparkled.music.SequencePlayerServiceImpl;
import io.sparkled.music.SequenceSchedulerService;

public class MusicPlayerModule extends AbstractModule {

    @Override
    protected void configure() {
        bind(SequenceSchedulerService.class).to(SequenceSchedulerServiceImpl.class);
        bind(SequencePlayerService.class).to(SequencePlayerServiceImpl.class);
    }
}

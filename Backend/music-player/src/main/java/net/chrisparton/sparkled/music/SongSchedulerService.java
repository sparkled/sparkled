package net.chrisparton.sparkled.music;

import net.chrisparton.sparkled.model.entity.ScheduledSong;

/**
 * Uses {@link ScheduledSong} records to schedule and play songs at appropriate times.
 */
public interface SongSchedulerService {

    /**
     * Load the next scheduled song and listen for further song publishing events.
     */
    void start();
}

package net.chrisparton.sparkled.event;

import net.chrisparton.sparkled.entity.ScheduledSong;

public class SongScheduledEvent {
    private ScheduledSong scheduledSong;

    public SongScheduledEvent(ScheduledSong scheduledSong) {
        this.scheduledSong = scheduledSong;
    }

    public ScheduledSong getScheduledSong() {
        return scheduledSong;
    }
}

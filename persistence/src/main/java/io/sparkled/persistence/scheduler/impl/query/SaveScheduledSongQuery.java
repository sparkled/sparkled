package io.sparkled.persistence.scheduler.impl.query;

import io.sparkled.model.entity.ScheduledSong;
import io.sparkled.model.entity.Song;
import io.sparkled.persistence.PersistenceQuery;
import io.sparkled.persistence.song.impl.query.GetSongByIdQuery;
import org.apache.commons.lang3.time.DateUtils;

import javax.persistence.EntityManager;
import java.util.Date;
import java.util.Optional;

public class SaveScheduledSongQuery implements PersistenceQuery<Boolean> {

    private final ScheduledSong scheduledSong;

    public SaveScheduledSongQuery(ScheduledSong scheduledSong) {
        this.scheduledSong = scheduledSong;
    }

    @Override
    public Boolean perform(EntityManager entityManager) {
        Song providedSong = scheduledSong.getSong();
        if (providedSong == null) {
            return false;
        }

        Optional<Song> song = new GetSongByIdQuery(providedSong.getId()).perform(entityManager);
        if (!song.isPresent()) {
            return false;
        }

        ScheduledSong scheduledSong = this.scheduledSong;
        scheduledSong.setEndTime(calculateEndTime(scheduledSong));
        scheduledSong = entityManager.merge(scheduledSong);
        return scheduledSong != null;
    }

    private Date calculateEndTime(ScheduledSong scheduledSong) {
        Song song = scheduledSong.getSong();
        Date endTime = null;

        if (song != null) {
            Date startTime = scheduledSong.getStartTime();
            int durationSeconds = (int) Math.ceil(song.getDurationFrames() / (double) song.getFramesPerSecond());
            endTime = DateUtils.addSeconds(startTime, durationSeconds);
        }

        return endTime;
    }
}

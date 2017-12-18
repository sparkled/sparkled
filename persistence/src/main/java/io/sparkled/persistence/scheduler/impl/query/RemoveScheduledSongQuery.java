package io.sparkled.persistence.scheduler.impl.query;

import io.sparkled.model.entity.ScheduledSong;
import io.sparkled.persistence.PersistenceQuery;

import javax.persistence.EntityManager;
import java.util.Optional;

public class RemoveScheduledSongQuery implements PersistenceQuery<Boolean> {

    private final int scheduledSongId;

    public RemoveScheduledSongQuery(int scheduledSongId) {
        this.scheduledSongId = scheduledSongId;
    }

    @Override
    public Boolean perform(EntityManager entityManager) {
        Optional<ScheduledSong> song = new GetScheduledSongByIdQuery(scheduledSongId).perform(entityManager);
        if (song.isPresent()) {
            entityManager.remove(song.get());
            return true;
        }

        return false;
    }
}

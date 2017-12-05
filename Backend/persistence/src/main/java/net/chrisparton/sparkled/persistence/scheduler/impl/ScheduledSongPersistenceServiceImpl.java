package net.chrisparton.sparkled.persistence.scheduler.impl;

import com.google.inject.persist.Transactional;
import net.chrisparton.sparkled.model.entity.ScheduledSong;
import net.chrisparton.sparkled.model.entity.Song;
import net.chrisparton.sparkled.persistence.scheduler.ScheduledSongPersistenceService;
import net.chrisparton.sparkled.persistence.scheduler.impl.query.*;

import javax.inject.Inject;
import javax.inject.Provider;
import javax.persistence.EntityManager;
import java.util.Date;
import java.util.List;
import java.util.Optional;

public class ScheduledSongPersistenceServiceImpl implements ScheduledSongPersistenceService {

    private Provider<EntityManager> entityManagerProvider;

    @Inject
    public ScheduledSongPersistenceServiceImpl(Provider<EntityManager> entityManagerProvider) {
        this.entityManagerProvider = entityManagerProvider;
    }

    @Override
    @Transactional
    public Optional<ScheduledSong> getNextScheduledSong() {
        return new GetNextScheduledSongQuery().perform(entityManagerProvider.get());
    }

    @Override
    public Optional<Song> getNextAutoSchedulableSong(int lastSongId) {
        return new GetNextAutoSchedulableSongQuery(lastSongId).perform(entityManagerProvider.get());
    }

    @Override
    @Transactional
    public List<ScheduledSong> getScheduledSongs(Date startDate, Date endDate) {
        return new GetScheduledSongsQuery(startDate, endDate).perform(entityManagerProvider.get());
    }

    @Override
    @Transactional
    public Optional<ScheduledSong> getScheduledSongAtTime(Date time) {
        return new GetScheduledSongAtTimeQuery(time).perform(entityManagerProvider.get());
    }

    @Override
    @Transactional
    public boolean removeScheduledSong(int scheduledSongId) {
        return new RemoveScheduledSongQuery(scheduledSongId).perform(entityManagerProvider.get());
    }

    @Override
    @Transactional
    public boolean saveScheduledSong(ScheduledSong scheduledSong) {
        return new SaveScheduledSongQuery(scheduledSong).perform(entityManagerProvider.get());
    }
}

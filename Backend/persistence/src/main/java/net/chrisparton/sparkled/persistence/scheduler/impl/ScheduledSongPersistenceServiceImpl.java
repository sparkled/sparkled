package net.chrisparton.sparkled.persistence.scheduler.impl;

import com.google.inject.persist.Transactional;
import net.chrisparton.sparkled.model.entity.ScheduledSong;
import net.chrisparton.sparkled.persistence.scheduler.ScheduledSongPersistenceService;
import net.chrisparton.sparkled.persistence.scheduler.impl.query.GetNextScheduledSongQuery;
import net.chrisparton.sparkled.persistence.scheduler.impl.query.GetScheduledSongsQuery;
import net.chrisparton.sparkled.persistence.scheduler.impl.query.RemoveScheduledSongQuery;
import net.chrisparton.sparkled.persistence.scheduler.impl.query.SaveScheduledSongQuery;
import net.chrisparton.sparkled.persistence.song.impl.SongPersistenceServiceImpl;

import javax.inject.Inject;
import javax.inject.Provider;
import javax.persistence.EntityManager;
import java.util.Date;
import java.util.List;
import java.util.Optional;

public class ScheduledSongPersistenceServiceImpl implements ScheduledSongPersistenceService {

    private Provider<EntityManager> entityManagerProvider;
    private SongPersistenceServiceImpl songPersistenceService;

    @Inject
    public ScheduledSongPersistenceServiceImpl(Provider<EntityManager> entityManagerProvider,
                                               SongPersistenceServiceImpl songPersistenceService) {
        this.entityManagerProvider = entityManagerProvider;
        this.songPersistenceService = songPersistenceService;
    }

    @Override
    @Transactional
    public Optional<ScheduledSong> getNextScheduledSong() {
        return new GetNextScheduledSongQuery().perform(entityManagerProvider.get());
    }

    @Override
    @Transactional
    public List<ScheduledSong> getScheduledSongs(Date startDate, Date endDate) {
        return new GetScheduledSongsQuery(startDate, endDate).perform(entityManagerProvider.get());
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

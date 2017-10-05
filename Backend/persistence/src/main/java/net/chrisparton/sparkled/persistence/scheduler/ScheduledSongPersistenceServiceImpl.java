package net.chrisparton.sparkled.persistence.scheduler;

import com.google.inject.persist.Transactional;
import net.chrisparton.sparkled.entity.ScheduledSong;
import net.chrisparton.sparkled.entity.ScheduledSong_;
import net.chrisparton.sparkled.entity.Song;
import net.chrisparton.sparkled.persistence.song.SongPersistenceServiceImpl;
import org.apache.commons.lang3.time.DateUtils;

import javax.inject.Inject;
import javax.inject.Provider;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
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
        final EntityManager entityManager = entityManagerProvider.get();

        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<ScheduledSong> cq = cb.createQuery(ScheduledSong.class);
        Root<ScheduledSong> root = cq.from(ScheduledSong.class);

        cq.where(
                cb.greaterThan(root.get(ScheduledSong_.startTime), new Date())
        );

        cq.orderBy(
                cb.asc(root.get(ScheduledSong_.startTime))
        );

        TypedQuery<ScheduledSong> query = entityManager.createQuery(cq);
        query.setMaxResults(1);

        try {
            ScheduledSong scheduledSong = query.getSingleResult();
            return Optional.ofNullable(scheduledSong);
        } catch (NoResultException e) {
            return Optional.empty();
        }
    }

    @Override
    @Transactional
    public List<ScheduledSong> getScheduledSongs(Date startDate, Date endDate) {
        final EntityManager entityManager = entityManagerProvider.get();

        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<ScheduledSong> cq = cb.createQuery(ScheduledSong.class);
        Root<ScheduledSong> root = cq.from(ScheduledSong.class);

        cq.where(
                cb.or(
                        cb.between(root.get(ScheduledSong_.startTime), startDate, endDate),
                        cb.between(root.get(ScheduledSong_.endTime), startDate, endDate)
                )
        );

        cq.orderBy(
                cb.asc(root.get(ScheduledSong_.startTime))
        );

        TypedQuery<ScheduledSong> query = entityManager.createQuery(cq);
        return query.getResultList();
    }

    @Override
    @Transactional
    public boolean removeScheduledSong(int scheduledSongId) {
        final EntityManager entityManager = entityManagerProvider.get();

        Optional<ScheduledSong> song = getScheduledSongById(entityManager, scheduledSongId);
        if (song.isPresent()) {
            entityManager.remove(song.get());
            return true;
        }

        return false;
    }

    private Optional<ScheduledSong> getScheduledSongById(EntityManager entityManager, int scheduledSongId) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<ScheduledSong> cq = cb.createQuery(ScheduledSong.class);
        Root<ScheduledSong> song = cq.from(ScheduledSong.class);
        cq.where(
                cb.equal(song.get(ScheduledSong_.id), scheduledSongId)
        );

        TypedQuery<ScheduledSong> query = entityManager.createQuery(cq);

        List<ScheduledSong> scheduledSongs = query.getResultList();
        if (!scheduledSongs.isEmpty()) {
            return Optional.of(scheduledSongs.get(0));
        } else {
            return Optional.empty();
        }
    }

    @Override
    @Transactional
    public boolean saveScheduledSong(ScheduledSong scheduledSong) {
        Song providedSong = scheduledSong.getSong();
        if (providedSong == null) {
            return false;
        }

        Optional<Song> song = songPersistenceService.getSongById(providedSong.getId());
        if (!song.isPresent()) {
            return false;
        }

        scheduledSong.setEndTime(calculateEndTime(scheduledSong));

        final EntityManager entityManager = entityManagerProvider.get();
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

package net.chrisparton.sparkled.persistence.scheduler;

import net.chrisparton.sparkled.entity.ScheduledSong;
import net.chrisparton.sparkled.entity.ScheduledSong_;
import net.chrisparton.sparkled.entity.Song;
import net.chrisparton.sparkled.persistence.PersistenceService;
import net.chrisparton.sparkled.persistence.song.SongPersistenceService;
import org.apache.commons.lang3.time.DateUtils;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.Date;
import java.util.List;
import java.util.Optional;

public class ScheduledSongPersistenceService {

    private SongPersistenceService songPersistenceService = new SongPersistenceService();

    public List<ScheduledSong> getScheduledSongs(Date startDate, Date endDate) {

        return PersistenceService.instance().perform(
                entityManager -> this.getScheduledSongs(entityManager, startDate, endDate)
        );
    }

    private List<ScheduledSong> getScheduledSongs(EntityManager entityManager, Date startDate, Date endDate) {
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

    public boolean removeScheduledSong(int scheduledSongId) {
        return PersistenceService.instance().perform(
                entityManager -> removeScheduledSong(entityManager, scheduledSongId)
        );
    }

    private boolean removeScheduledSong(EntityManager entityManager, int scheduledSongId) {
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

        return PersistenceService.instance().perform(
                entityManager -> saveScheduledSong(entityManager, scheduledSong)
        );
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

    private boolean saveScheduledSong(EntityManager entityManager, ScheduledSong scheduledSong) {
        scheduledSong = entityManager.merge(scheduledSong);
        return scheduledSong != null;
    }
}

package net.chrisparton.sparkled.persistence.scheduler.impl.query;

import net.chrisparton.sparkled.entity.ScheduledSong;
import net.chrisparton.sparkled.entity.ScheduledSong_;
import net.chrisparton.sparkled.persistence.PersistenceQuery;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.List;
import java.util.Optional;

public class GetScheduledSongByIdQuery implements PersistenceQuery<Optional<ScheduledSong>> {

    private final int scheduledSongId;

    public GetScheduledSongByIdQuery(int scheduledSongId) {
        this.scheduledSongId = scheduledSongId;
    }

    @Override
    public Optional<ScheduledSong> perform(EntityManager entityManager) {
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
}

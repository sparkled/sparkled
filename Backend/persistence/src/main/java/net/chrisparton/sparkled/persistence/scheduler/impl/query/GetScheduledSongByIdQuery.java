package net.chrisparton.sparkled.persistence.scheduler.impl.query;

import net.chrisparton.sparkled.model.entity.ScheduledSong;
import net.chrisparton.sparkled.model.entity.ScheduledSong_;
import net.chrisparton.sparkled.persistence.PersistenceQuery;
import net.chrisparton.sparkled.persistence.util.PersistenceUtils;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
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
        Root<ScheduledSong> scheduledSong = cq.from(ScheduledSong.class);
        cq.where(
                cb.equal(scheduledSong.get(ScheduledSong_.id), scheduledSongId)
        );

        TypedQuery<ScheduledSong> query = entityManager.createQuery(cq);
        return PersistenceUtils.getSingleResult(query);
    }
}

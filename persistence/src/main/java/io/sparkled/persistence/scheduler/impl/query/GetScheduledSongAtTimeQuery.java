package io.sparkled.persistence.scheduler.impl.query;

import io.sparkled.model.entity.ScheduledSong;
import io.sparkled.model.entity.ScheduledSong_;
import io.sparkled.persistence.PersistenceQuery;
import io.sparkled.persistence.util.PersistenceUtils;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.Date;
import java.util.Optional;

public class GetScheduledSongAtTimeQuery implements PersistenceQuery<Optional<ScheduledSong>> {

    private final Date time;

    public GetScheduledSongAtTimeQuery(Date time) {
        this.time = time;
    }

    @Override
    public Optional<ScheduledSong> perform(EntityManager entityManager) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<ScheduledSong> cq = cb.createQuery(ScheduledSong.class);
        Root<ScheduledSong> scheduledSong = cq.from(ScheduledSong.class);

        cq.where(
                cb.and(
                        cb.greaterThanOrEqualTo(scheduledSong.get(ScheduledSong_.endTime), time),
                        cb.lessThanOrEqualTo(scheduledSong.get(ScheduledSong_.startTime), time)
                )
        );

        TypedQuery<ScheduledSong> query = entityManager.createQuery(cq);
        query.setMaxResults(1);
        return PersistenceUtils.getSingleResult(query);
    }
}

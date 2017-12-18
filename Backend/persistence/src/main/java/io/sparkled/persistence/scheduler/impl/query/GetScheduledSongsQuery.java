package io.sparkled.persistence.scheduler.impl.query;

import io.sparkled.model.entity.ScheduledSong;
import io.sparkled.model.entity.ScheduledSong_;
import io.sparkled.persistence.PersistenceQuery;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.Date;
import java.util.List;

public class GetScheduledSongsQuery implements PersistenceQuery<List<ScheduledSong>> {

    private final Date startDate;
    private final Date endDate;

    public GetScheduledSongsQuery(Date startDate, Date endDate) {
        this.startDate = startDate;
        this.endDate = endDate;
    }

    @Override
    public List<ScheduledSong> perform(EntityManager entityManager) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<ScheduledSong> cq = cb.createQuery(ScheduledSong.class);
        Root<ScheduledSong> scheduledSong = cq.from(ScheduledSong.class);

        cq.where(
                cb.or(
                        cb.between(scheduledSong.get(ScheduledSong_.startTime), startDate, endDate),
                        cb.between(scheduledSong.get(ScheduledSong_.endTime), startDate, endDate)
                )
        );

        cq.orderBy(
                cb.asc(scheduledSong.get(ScheduledSong_.startTime))
        );

        TypedQuery<ScheduledSong> query = entityManager.createQuery(cq);
        return query.getResultList();
    }
}

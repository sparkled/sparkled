package io.sparkled.persistence.scheduler.impl.query;

import io.sparkled.model.entity.ScheduledSequence;
import io.sparkled.model.entity.ScheduledSequence_;
import io.sparkled.persistence.PersistenceQuery;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.Date;
import java.util.List;

public class GetScheduledSequencesQuery implements PersistenceQuery<List<ScheduledSequence>> {

    private final Date startDate;
    private final Date endDate;

    public GetScheduledSequencesQuery(Date startDate, Date endDate) {
        this.startDate = startDate;
        this.endDate = endDate;
    }

    @Override
    public List<ScheduledSequence> perform(EntityManager entityManager) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<ScheduledSequence> cq = cb.createQuery(ScheduledSequence.class);
        Root<ScheduledSequence> scheduledSequence = cq.from(ScheduledSequence.class);

        cq.where(
                cb.or(
                        cb.between(scheduledSequence.get(ScheduledSequence_.startTime), startDate, endDate),
                        cb.between(scheduledSequence.get(ScheduledSequence_.endTime), startDate, endDate)
                )
        );

        cq.orderBy(
                cb.asc(scheduledSequence.get(ScheduledSequence_.startTime))
        );

        TypedQuery<ScheduledSequence> query = entityManager.createQuery(cq);
        return query.getResultList();
    }
}

package io.sparkled.persistence.scheduler.impl.query;

import io.sparkled.model.entity.ScheduledSequence;
import io.sparkled.model.entity.ScheduledSequence_;
import io.sparkled.persistence.PersistenceQuery;
import io.sparkled.persistence.util.PersistenceUtils;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.Date;
import java.util.Optional;

public class GetNextScheduledSequenceQuery implements PersistenceQuery<Optional<ScheduledSequence>> {

    @Override
    public Optional<ScheduledSequence> perform(EntityManager entityManager) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<ScheduledSequence> cq = cb.createQuery(ScheduledSequence.class);
        Root<ScheduledSequence> scheduledSequence = cq.from(ScheduledSequence.class);

        cq.where(
                cb.greaterThan(scheduledSequence.get(ScheduledSequence_.startTime), new Date())
        );

        cq.orderBy(
                cb.asc(scheduledSequence.get(ScheduledSequence_.startTime))
        );

        TypedQuery<ScheduledSequence> query = entityManager.createQuery(cq);
        query.setMaxResults(1);

        return PersistenceUtils.getSingleResult(query);
    }
}

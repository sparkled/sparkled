package io.sparkled.persistence.scheduler.impl.query;

import io.sparkled.model.entity.ScheduledSequence;
import io.sparkled.model.entity.ScheduledSequence_;
import io.sparkled.model.entity.Sequence;
import io.sparkled.model.entity.Sequence_;
import io.sparkled.persistence.PersistenceQuery;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.Root;
import java.util.List;

public class GetScheduledSequencesBySequenceIdQuery implements PersistenceQuery<List<ScheduledSequence>> {

    private final int sequenceId;

    public GetScheduledSequencesBySequenceIdQuery(int sequenceId) {
        this.sequenceId = sequenceId;
    }

    @Override
    public List<ScheduledSequence> perform(EntityManager entityManager) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<ScheduledSequence> cq = cb.createQuery(ScheduledSequence.class);
        Root<ScheduledSequence> scheduledSequence = cq.from(ScheduledSequence.class);
        Join<ScheduledSequence, Sequence> sequence = scheduledSequence.join(ScheduledSequence_.sequence);
        cq.where(
                cb.equal(sequence.get(Sequence_.id), sequenceId)
        );

        TypedQuery<ScheduledSequence> query = entityManager.createQuery(cq);
        return query.getResultList();
    }
}

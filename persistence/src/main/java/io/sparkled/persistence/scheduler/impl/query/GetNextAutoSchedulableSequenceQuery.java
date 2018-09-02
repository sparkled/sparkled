package io.sparkled.persistence.scheduler.impl.query;

import io.sparkled.model.entity.Sequence;
import io.sparkled.model.entity.Sequence_;
import io.sparkled.persistence.PersistenceQuery;
import io.sparkled.persistence.util.PersistenceUtils;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.Optional;

public class GetNextAutoSchedulableSequenceQuery implements PersistenceQuery<Optional<Sequence>> {

    private int previousSequenceId;

    public GetNextAutoSchedulableSequenceQuery(int previousSequenceId) {
        this.previousSequenceId = previousSequenceId;
    }

    @Override
    public Optional<Sequence> perform(EntityManager entityManager) {
        Optional<Sequence> result = query(entityManager, previousSequenceId);
        if (!result.isPresent()) {
            // The last auto schedulable sequence has been played, so start from the beginning.
            result = query(entityManager, 0);
        }
        return result;
    }

    private Optional<Sequence> query(EntityManager entityManager, int previousSequenceId) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Sequence> cq = cb.createQuery(Sequence.class);
        Root<Sequence> sequence = cq.from(Sequence.class);

        cq.where(
                cb.and(
                        cb.equal(sequence.get(Sequence_.autoSchedulable), true),
                        cb.greaterThan(sequence.get(Sequence_.id), previousSequenceId)
                )
        );

        cq.orderBy(
                cb.asc(sequence.get(Sequence_.id))
        );

        TypedQuery<Sequence> query = entityManager.createQuery(cq);
        query.setMaxResults(1);

        return PersistenceUtils.getSingleResult(query);
    }
}

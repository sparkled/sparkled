package io.sparkled.persistence.sequence.impl.query;

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

public class GetSequenceByIdQuery implements PersistenceQuery<Optional<Sequence>> {

    private final int sequenceId;

    public GetSequenceByIdQuery(int sequenceId) {
        this.sequenceId = sequenceId;
    }

    @Override
    public Optional<Sequence> perform(EntityManager entityManager) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Sequence> cq = cb.createQuery(Sequence.class);
        Root<Sequence> sequence = cq.from(Sequence.class);
        cq.where(
                cb.equal(sequence.get(Sequence_.id), sequenceId)
        );

        TypedQuery<Sequence> query = entityManager.createQuery(cq);
        return PersistenceUtils.getSingleResult(query);
    }
}

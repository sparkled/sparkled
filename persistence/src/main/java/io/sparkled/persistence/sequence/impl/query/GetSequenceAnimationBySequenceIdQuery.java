package io.sparkled.persistence.sequence.impl.query;

import io.sparkled.model.entity.SequenceAnimation;
import io.sparkled.model.entity.SequenceAnimation_;
import io.sparkled.persistence.PersistenceQuery;
import io.sparkled.persistence.util.PersistenceUtils;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.Optional;

public class GetSequenceAnimationBySequenceIdQuery implements PersistenceQuery<Optional<SequenceAnimation>> {

    private final int sequenceId;

    public GetSequenceAnimationBySequenceIdQuery(int sequenceId) {
        this.sequenceId = sequenceId;
    }

    @Override
    public Optional<SequenceAnimation> perform(EntityManager entityManager) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<SequenceAnimation> cq = cb.createQuery(SequenceAnimation.class);
        Root<SequenceAnimation> sequenceAnimation = cq.from(SequenceAnimation.class);
        cq.where(
                cb.equal(sequenceAnimation.get(SequenceAnimation_.sequenceId), sequenceId)
        );

        TypedQuery<SequenceAnimation> query = entityManager.createQuery(cq);
        return PersistenceUtils.getSingleResult(query);
    }
}

package io.sparkled.persistence.sequence.impl.query;

import io.sparkled.model.entity.Sequence;
import io.sparkled.model.entity.Sequence_;
import io.sparkled.persistence.PersistenceQuery;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.List;

public class GetSequencesByStageIdQuery implements PersistenceQuery<List<Sequence>> {

    private final int stageId;

    public GetSequencesByStageIdQuery(int stageId) {
        this.stageId = stageId;
    }

    @Override
    public List<Sequence> perform(EntityManager entityManager) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Sequence> cq = cb.createQuery(Sequence.class);
        Root<Sequence> sequence = cq.from(Sequence.class);

        cq.where(cb.equal(sequence.get(Sequence_.stageId), stageId));

        TypedQuery<Sequence> query = entityManager.createQuery(cq);
        return query.getResultList();
    }
}

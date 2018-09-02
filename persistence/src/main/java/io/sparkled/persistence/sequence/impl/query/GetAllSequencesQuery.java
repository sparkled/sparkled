package io.sparkled.persistence.sequence.impl.query;

import io.sparkled.model.entity.Sequence_;
import io.sparkled.model.entity.Sequence;
import io.sparkled.persistence.PersistenceQuery;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.List;

public class GetAllSequencesQuery implements PersistenceQuery<List<Sequence>> {

    @Override
    public List<Sequence> perform(EntityManager entityManager) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Sequence> cq = cb.createQuery(Sequence.class);
        Root<Sequence> sequence = cq.from(Sequence.class);

        cq.orderBy(
                cb.asc(sequence.get(Sequence_.artist)),
                cb.asc(sequence.get(Sequence_.album)),
                cb.asc(sequence.get(Sequence_.name))
        );

        TypedQuery<Sequence> query = entityManager.createQuery(cq);
        return query.getResultList();
    }
}

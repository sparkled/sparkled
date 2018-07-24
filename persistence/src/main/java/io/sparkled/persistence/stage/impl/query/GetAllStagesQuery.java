package io.sparkled.persistence.stage.impl.query;

import io.sparkled.model.entity.Stage_;
import io.sparkled.model.entity.Stage;
import io.sparkled.persistence.PersistenceQuery;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.List;

public class GetAllStagesQuery implements PersistenceQuery<List<Stage>> {

    @Override
    public List<Stage> perform(EntityManager entityManager) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Stage> cq = cb.createQuery(Stage.class);
        Root<Stage> stage = cq.from(Stage.class);

        cq.orderBy(
                cb.asc(stage.get(Stage_.name))
        );

        TypedQuery<Stage> query = entityManager.createQuery(cq);
        return query.getResultList();
    }
}

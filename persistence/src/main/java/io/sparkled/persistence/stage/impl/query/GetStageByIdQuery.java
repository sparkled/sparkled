package io.sparkled.persistence.stage.impl.query;

import io.sparkled.model.entity.Stage;
import io.sparkled.model.entity.StageProp;
import io.sparkled.model.entity.Stage_;
import io.sparkled.persistence.PersistenceQuery;
import io.sparkled.persistence.util.PersistenceUtils;

import javax.persistence.EntityManager;
import javax.persistence.FetchType;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.*;
import java.util.Optional;

public class GetStageByIdQuery implements PersistenceQuery<Optional<Stage>> {

    private final int stageId;

    public GetStageByIdQuery(int stageId) {
        this.stageId = stageId;
    }

    @Override
    public Optional<Stage> perform(EntityManager entityManager) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Stage> cq = cb.createQuery(Stage.class);
        Root<Stage> stage = cq.from(Stage.class);
        cq.where(
                cb.equal(stage.get(Stage_.id), stageId)
        );

        TypedQuery<Stage> query = entityManager.createQuery(cq);
        return PersistenceUtils.getSingleResult(query);
    }
}

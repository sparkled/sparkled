package net.chrisparton.sparkled.persistence.stage.impl.query;

import net.chrisparton.sparkled.model.entity.Stage;
import net.chrisparton.sparkled.model.entity.Stage_;
import net.chrisparton.sparkled.persistence.PersistenceQuery;
import net.chrisparton.sparkled.persistence.util.PersistenceUtils;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
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

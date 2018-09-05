package io.sparkled.persistence.sequence.impl.query;

import io.sparkled.model.entity.Stage;
import io.sparkled.persistence.PersistenceQuery;
import io.sparkled.persistence.util.PersistenceUtils;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.util.Optional;

public class GetStageBySequenceIdQuery implements PersistenceQuery<Optional<Stage>> {

    private final int sequenceId;

    public GetStageBySequenceIdQuery(int sequenceId) {
        this.sequenceId = sequenceId;
    }

    @Override
    public Optional<Stage> perform(EntityManager entityManager) {
        Optional<Integer> stageId = getSequenceId(entityManager);

        if (stageId.isPresent()) {
            return getStage(entityManager, stageId.get());
        } else {
            return Optional.empty();
        }
    }

    private Optional<Integer> getSequenceId(EntityManager entityManager) {
        String hql = "select stageId from Sequence where id = :sequenceId";
        TypedQuery<Integer> query = entityManager.createQuery(hql, Integer.class);

        query.setParameter("sequenceId", sequenceId);
        return PersistenceUtils.getSingleResult(query);
    }

    private Optional<Stage> getStage(EntityManager entityManager, Integer stageId) {
        String hql = "from Stage where id = :stageId";
        TypedQuery<Stage> query = entityManager.createQuery(hql, Stage.class);

        query.setParameter("stageId", stageId);
        return PersistenceUtils.getSingleResult(query);
    }
}

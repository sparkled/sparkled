package net.chrisparton.sparkled.persistence.stage;

import com.google.inject.persist.Transactional;
import net.chrisparton.sparkled.entity.Stage;
import net.chrisparton.sparkled.entity.Stage_;

import javax.inject.Inject;
import javax.inject.Provider;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.List;
import java.util.Optional;

public class StagePersistenceServiceImpl implements StagePersistenceService {

    private Provider<EntityManager> entityManagerProvider;

    @Inject
    public StagePersistenceServiceImpl(Provider<EntityManager> entityManagerProvider) {
        this.entityManagerProvider = entityManagerProvider;
    }

    @Override
    @Transactional
    public Optional<Stage> getStageById(int stageId) {
        final EntityManager entityManager = entityManagerProvider.get();

        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Stage> cq = cb.createQuery(Stage.class);
        Root<Stage> stage = cq.from(Stage.class);
        cq.where(
                cb.equal(stage.get(Stage_.id), stageId)
        );

        TypedQuery<Stage> query = entityManager.createQuery(cq);

        List<Stage> stages = query.getResultList();
        if (!stages.isEmpty()) {
            return Optional.of(stages.get(0));
        } else {
            return Optional.empty();
        }
    }
}

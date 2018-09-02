package io.sparkled.persistence.sequence.impl.query;

import io.sparkled.model.entity.RenderedStageProp;
import io.sparkled.persistence.PersistenceQuery;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.logging.Logger;

public class DeleteRenderedStagePropsQuery implements PersistenceQuery<Void> {

    private static final Logger logger = Logger.getLogger(DeleteRenderedStagePropsQuery.class.getName());

    private final int sequenceId;

    public DeleteRenderedStagePropsQuery(int sequenceId) {
        this.sequenceId = sequenceId;
    }

    @Override
    public Void perform(EntityManager entityManager) {
        String className = RenderedStageProp.class.getSimpleName();
        Query query = entityManager.createQuery("delete from " + className + " where sequenceId = :id");
        query.setParameter("id", sequenceId);

        int deleted = query.executeUpdate();
        logger.info("Deleted " + deleted + " " + className + " record(s) for sequence " + sequenceId);
        return null;
    }
}

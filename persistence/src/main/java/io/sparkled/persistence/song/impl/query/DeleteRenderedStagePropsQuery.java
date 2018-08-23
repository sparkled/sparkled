package io.sparkled.persistence.song.impl.query;

import io.sparkled.model.entity.RenderedStageProp;
import io.sparkled.persistence.PersistenceQuery;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.logging.Logger;

public class DeleteRenderedStagePropsQuery implements PersistenceQuery<Void> {

    private static final Logger logger = Logger.getLogger(DeleteRenderedStagePropsQuery.class.getName());

    private final int songId;

    public DeleteRenderedStagePropsQuery(int songId) {
        this.songId = songId;
    }

    @Override
    public Void perform(EntityManager entityManager) {
        String className = RenderedStageProp.class.getSimpleName();
        Query query = entityManager.createQuery("delete from " + className + " where songId = :id");
        query.setParameter("id", songId);

        int deleted = query.executeUpdate();
        logger.info("Deleted " + deleted + " " + className + " record(s) for song " + songId);
        return null;
    }
}

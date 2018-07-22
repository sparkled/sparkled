package io.sparkled.persistence.song.impl.query;

import io.sparkled.persistence.PersistenceQuery;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.logging.Logger;

public class DeleteRenderedChannelsQuery implements PersistenceQuery<Integer> {

    private static final Logger logger = Logger.getLogger(DeleteRenderedChannelsQuery.class.getName());

    private final int songId;

    public DeleteRenderedChannelsQuery(int songId) {
        this.songId = songId;
    }

    @Override
    public Integer perform(EntityManager entityManager) {
        Query query = entityManager.createQuery("delete from RenderedChannelData where songId = :id");
        query.setParameter("id", songId);

        int deleted = query.executeUpdate();
        logger.info("Deleted " + deleted + " RenderedChannelData record(s) for song " + songId);
        return deleted;
    }
}

package io.sparkled.persistence.playlist.impl.query;

import io.sparkled.model.entity.Playlist;
import io.sparkled.model.validator.PlaylistValidator;
import io.sparkled.persistence.PersistenceQuery;
import io.sparkled.persistence.QueryFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.EntityManager;

public class SavePlaylistQuery implements PersistenceQuery<Playlist> {

    private static final Logger logger = LoggerFactory.getLogger(SavePlaylistQuery.class);
    private final Playlist playlist;

    public SavePlaylistQuery(Playlist playlist) {
        this.playlist = playlist;
    }

    @Override
    public Playlist perform(QueryFactory queryFactory) {
        new PlaylistValidator().validate(playlist);

        EntityManager entityManager = queryFactory.getEntityManager();
        Playlist result = entityManager.merge(playlist);

        logger.info("Saved playlist {} ({}).", playlist.getId(), playlist.getName());
        return result;
    }
}

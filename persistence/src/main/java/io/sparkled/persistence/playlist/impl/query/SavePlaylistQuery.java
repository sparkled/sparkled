package io.sparkled.persistence.playlist.impl.query;

import io.sparkled.model.entity.Playlist;
import io.sparkled.model.validator.PlaylistValidator;
import io.sparkled.persistence.PersistenceQuery;
import io.sparkled.persistence.QueryFactory;

import javax.persistence.EntityManager;

public class SavePlaylistQuery implements PersistenceQuery<Integer> {

    private final Playlist playlist;

    public SavePlaylistQuery(Playlist playlist) {
        this.playlist = playlist;
    }

    @Override
    public Integer perform(QueryFactory queryFactory) {
        EntityManager entityManager = queryFactory.getEntityManager();

        new PlaylistValidator(playlist).validate();

        Playlist result = entityManager.merge(playlist);
        // TODO: Handle playlist sequences.
        return result.getId();
    }
}

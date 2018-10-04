package io.sparkled.persistence.playlist.impl.query;

import io.sparkled.model.entity.PlaylistSequence;
import io.sparkled.model.validator.PlaylistSequenceValidator;
import io.sparkled.persistence.PersistenceQuery;
import io.sparkled.persistence.QueryFactory;

import javax.persistence.EntityManager;
import java.util.List;

public class SavePlaylistSequencesQuery implements PersistenceQuery<Void> {

    private final List<PlaylistSequence> playlistSequences;

    public SavePlaylistSequencesQuery(List<PlaylistSequence> playlistSequences) {
        this.playlistSequences = playlistSequences;
    }

    @Override
    public Void perform(QueryFactory queryFactory) {
        final EntityManager entityManager = queryFactory.getEntityManager();

        PlaylistSequenceValidator sequenceChannelValidator = new PlaylistSequenceValidator();
        playlistSequences.forEach(sequenceChannelValidator::validate);
        playlistSequences.forEach(entityManager::merge);
        return null;
    }
}

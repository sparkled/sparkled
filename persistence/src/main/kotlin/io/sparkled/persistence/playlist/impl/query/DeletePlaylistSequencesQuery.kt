package io.sparkled.persistence.playlist.impl.query;

import io.sparkled.persistence.PersistenceQuery;
import io.sparkled.persistence.QueryFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collection;
import java.util.UUID;

public class DeletePlaylistSequencesQuery implements PersistenceQuery<Void> {

    private static final Logger logger = LoggerFactory.getLogger(DeletePlaylistSequencesQuery.class);

    private final Collection<UUID> playlistSequenceUuids;

    public DeletePlaylistSequencesQuery(Collection<UUID> playlistSequenceUuids) {
        this.playlistSequenceUuids = playlistSequenceUuids.isEmpty() ? noUuids : playlistSequenceUuids;
    }

    @Override
    public Void perform(QueryFactory queryFactory) {
        long deleted = queryFactory
                .delete(qPlaylistSequence)
                .where(qPlaylistSequence.uuid.in(playlistSequenceUuids))
                .execute();

        logger.info("Deleted {} playlist sequence(s).", deleted);
        return null;
    }
}

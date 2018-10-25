package io.sparkled.persistence.playlist.impl.query;

import io.sparkled.persistence.PersistenceQuery;
import io.sparkled.persistence.QueryFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.UUID;

public class DeletePlaylistQuery implements PersistenceQuery<Void> {

    private static final Logger logger = LoggerFactory.getLogger(DeletePlaylistQuery.class);

    private final int playlistId;

    public DeletePlaylistQuery(int playlistId) {
        this.playlistId = playlistId;
    }

    @Override
    public Void perform(QueryFactory queryFactory) {
        deletePlaylistSequences(queryFactory);
        deletePlaylist(queryFactory);
        return null;
    }

    private void deletePlaylistSequences(QueryFactory queryFactory) {
        List<UUID> playlistSequenceUuids = queryFactory
                .select(qPlaylistSequence.uuid)
                .from(qPlaylistSequence)
                .where(qPlaylistSequence.playlistId.eq(playlistId))
                .fetch();
        new DeletePlaylistSequencesQuery(playlistSequenceUuids).perform(queryFactory);
    }

    private void deletePlaylist(QueryFactory queryFactory) {
        long deleted = queryFactory
                .delete(qPlaylist)
                .where(qPlaylist.id.eq(playlistId))
                .execute();

        logger.info("Deleted {} playlist(s).", deleted);
    }
}

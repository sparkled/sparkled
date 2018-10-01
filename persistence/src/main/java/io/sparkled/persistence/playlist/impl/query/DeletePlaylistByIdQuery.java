package io.sparkled.persistence.playlist.impl.query;

import io.sparkled.persistence.PersistenceQuery;
import io.sparkled.persistence.QueryFactory;

public class DeletePlaylistByIdQuery implements PersistenceQuery<Integer> {

    private final int playlistId;

    public DeletePlaylistByIdQuery(int playlistId) {
        this.playlistId = playlistId;
    }

    @Override
    public Integer perform(QueryFactory queryFactory) {
        removePlaylistSequences(queryFactory);
        removePlaylist(queryFactory);

        return playlistId;
    }

    private void removePlaylistSequences(QueryFactory queryFactory) {
        queryFactory
                .delete(qPlaylistSequence)
                .where(qPlaylistSequence.playlistId.eq(playlistId))
                .execute();
    }

    private void removePlaylist(QueryFactory queryFactory) {
        queryFactory
                .delete(qPlaylist)
                .where(qPlaylist.id.eq(playlistId))
                .execute();
    }
}

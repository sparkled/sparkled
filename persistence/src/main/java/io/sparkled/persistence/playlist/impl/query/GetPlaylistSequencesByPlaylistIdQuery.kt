package io.sparkled.persistence.playlist.impl.query;

import io.sparkled.model.entity.PlaylistSequence;
import io.sparkled.persistence.PersistenceQuery;
import io.sparkled.persistence.QueryFactory;

import java.util.List;

public class GetPlaylistSequencesByPlaylistIdQuery implements PersistenceQuery<List<PlaylistSequence>> {

    private final int playlistId;

    public GetPlaylistSequencesByPlaylistIdQuery(int playlistId) {
        this.playlistId = playlistId;
    }

    @Override
    public List<PlaylistSequence> perform(QueryFactory queryFactory) {
        return queryFactory
                .selectFrom(qPlaylistSequence)
                .where(qPlaylistSequence.playlistId.eq(playlistId))
                .orderBy(qPlaylistSequence.displayOrder.asc())
                .fetch();
    }
}

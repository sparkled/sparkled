package io.sparkled.persistence.playlist.impl.query;

import io.sparkled.model.entity.Playlist;
import io.sparkled.persistence.PersistenceQuery;
import io.sparkled.persistence.QueryFactory;

import java.util.List;

public class GetAllPlaylistsQuery implements PersistenceQuery<List<Playlist>> {

    @Override
    public List<Playlist> perform(QueryFactory queryFactory) {
        return queryFactory
                .selectFrom(qPlaylist)
                .orderBy(qPlaylist.name.asc())
                .fetch();
    }
}

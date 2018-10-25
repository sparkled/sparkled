package io.sparkled.persistence.playlist.impl.query

import io.sparkled.model.entity.Playlist
import io.sparkled.persistence.PersistenceQuery
import io.sparkled.persistence.QueryFactory

class GetAllPlaylistsQuery : PersistenceQuery<List<Playlist>> {

    @Override
    fun perform(queryFactory: QueryFactory): List<Playlist> {
        return queryFactory
                .selectFrom(qPlaylist)
                .orderBy(qPlaylist.name.asc())
                .fetch()
    }
}

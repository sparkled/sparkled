package io.sparkled.persistence.playlist.impl.query

import io.sparkled.model.entity.Playlist
import io.sparkled.model.entity.QPlaylist.playlist
import io.sparkled.persistence.PersistenceQuery
import io.sparkled.persistence.QueryFactory

class GetAllPlaylistsQuery : PersistenceQuery<List<Playlist>> {

    override fun perform(queryFactory: QueryFactory): List<Playlist> {
        return queryFactory
            .selectFrom(playlist)
            .orderBy(playlist.name.asc())
            .fetch()
    }
}

package io.sparkled.persistence.playlist.impl.query

import io.sparkled.model.entity.Playlist
import io.sparkled.model.entity.QPlaylist.Companion.playlist
import io.sparkled.persistence.PersistenceQuery
import io.sparkled.persistence.QueryFactory

class GetPlaylistByIdQuery(private val playlistId: Int) : PersistenceQuery<Playlist?> {

    override fun perform(queryFactory: QueryFactory): Playlist? {
        return queryFactory
            .selectFrom(playlist)
            .where(playlist.id.eq(playlistId))
            .fetchFirst()
    }
}

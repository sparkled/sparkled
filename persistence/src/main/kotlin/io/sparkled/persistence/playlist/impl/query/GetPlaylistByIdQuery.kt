package io.sparkled.persistence.playlist.impl.query

import io.sparkled.model.entity.Playlist
import io.sparkled.model.entity.QPlaylist.playlist
import io.sparkled.persistence.PersistenceQuery
import io.sparkled.persistence.QueryFactory
import java.util.Optional

class GetPlaylistByIdQuery(private val playlistId: Int) : PersistenceQuery<Optional<Playlist>> {

    override fun perform(queryFactory: QueryFactory): Optional<Playlist> {
        val playlist = queryFactory
            .selectFrom(playlist)
            .where(playlist.id.eq(playlistId))
            .fetchFirst()

        return Optional.ofNullable(playlist)
    }
}

package io.sparkled.persistence.playlist.impl.query

import io.sparkled.model.entity.PlaylistSequence
import io.sparkled.model.entity.QPlaylistSequence.Companion.playlistSequence
import io.sparkled.persistence.PersistenceQuery
import io.sparkled.persistence.QueryFactory

class GetPlaylistSequencesByPlaylistIdQuery(private val playlistId: Int) : PersistenceQuery<List<PlaylistSequence>> {

    override fun perform(queryFactory: QueryFactory): List<PlaylistSequence> {
        return queryFactory
            .selectFrom(playlistSequence)
            .where(playlistSequence.playlistId.eq(playlistId))
            .orderBy(playlistSequence.displayOrder.asc())
            .fetch()
    }
}

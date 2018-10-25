package io.sparkled.persistence.playlist.impl.query

import io.sparkled.model.entity.PlaylistSequence
import io.sparkled.persistence.PersistenceQuery
import io.sparkled.persistence.QueryFactory

class GetPlaylistSequencesByPlaylistIdQuery(private val playlistId: Int) : PersistenceQuery<List<PlaylistSequence>> {

    @Override
    fun perform(queryFactory: QueryFactory): List<PlaylistSequence> {
        return queryFactory
                .selectFrom(qPlaylistSequence)
                .where(qPlaylistSequence.playlistId.eq(playlistId))
                .orderBy(qPlaylistSequence.displayOrder.asc())
                .fetch()
    }
}

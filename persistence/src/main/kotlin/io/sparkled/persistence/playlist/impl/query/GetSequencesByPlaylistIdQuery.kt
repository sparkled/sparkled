package io.sparkled.persistence.playlist.impl.query

import io.sparkled.model.entity.QPlaylistSequence.Companion.playlistSequence
import io.sparkled.model.entity.QSequence.Companion.sequence
import io.sparkled.model.entity.Sequence
import io.sparkled.persistence.PersistenceQuery
import io.sparkled.persistence.QueryFactory

class GetSequencesByPlaylistIdQuery(private val playlistId: Int) : PersistenceQuery<List<Sequence>> {

    override fun perform(queryFactory: QueryFactory): List<Sequence> {
        return queryFactory
            .selectFrom(sequence)
            .innerJoin(playlistSequence).on(sequence.id.eq(playlistSequence.sequenceId))
            .where(playlistSequence.playlistId.eq(playlistId))
            .orderBy(playlistSequence.displayOrder.asc())
            .fetch()
    }
}

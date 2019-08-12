package io.sparkled.persistence.playlist.impl.query

import io.sparkled.model.entity.QPlaylistSequence.Companion.playlistSequence
import io.sparkled.model.entity.QSequence.Companion.sequence
import io.sparkled.model.entity.Sequence
import io.sparkled.persistence.PersistenceQuery
import io.sparkled.persistence.QueryFactory

class GetSequenceAtPlaylistIndexQuery(private val playlistId: Int, private val index: Int) :
    PersistenceQuery<Sequence?> {

    override fun perform(queryFactory: QueryFactory): Sequence? {
        return queryFactory
            .selectFrom(sequence)
            .innerJoin(playlistSequence).on(sequence.id.eq(playlistSequence.sequenceId))
            .where(playlistSequence.playlistId.eq(playlistId))
            .orderBy(playlistSequence.displayOrder.asc())
            .offset(index.toLong())
            .limit(1)
            .fetchFirst()
    }
}

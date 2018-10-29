package io.sparkled.persistence.playlist.impl.query

import io.sparkled.model.entity.QPlaylistSequence.playlistSequence
import io.sparkled.model.entity.QSequence.sequence
import io.sparkled.model.entity.Sequence
import io.sparkled.persistence.PersistenceQuery
import io.sparkled.persistence.QueryFactory
import java.util.Optional

class GetSequenceAtPlaylistIndexQuery(private val playlistId: Int, private val index: Int) :
    PersistenceQuery<Optional<Sequence>> {

    override fun perform(queryFactory: QueryFactory): Optional<Sequence> {
        val sequence = queryFactory
            .selectFrom(sequence)
            .innerJoin(playlistSequence).on(sequence.id.eq(playlistSequence.sequenceId))
            .where(playlistSequence.playlistId.eq(playlistId))
            .orderBy(playlistSequence.displayOrder.asc())
            .offset(index.toLong())
            .limit(1)
            .fetchFirst()

        return Optional.ofNullable(sequence)
    }
}

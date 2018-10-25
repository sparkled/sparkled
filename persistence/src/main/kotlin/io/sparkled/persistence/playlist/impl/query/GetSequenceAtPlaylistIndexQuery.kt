package io.sparkled.persistence.playlist.impl.query

import io.sparkled.model.entity.Sequence
import io.sparkled.persistence.PersistenceQuery
import io.sparkled.persistence.QueryFactory

import java.util.Optional

class GetSequenceAtPlaylistIndexQuery(private val playlistId: Int, private val index: Int) : PersistenceQuery<Optional<Sequence>> {

    @Override
    fun perform(queryFactory: QueryFactory): Optional<Sequence> {
        val sequence = queryFactory
                .selectFrom(qSequence)
                .innerJoin(qPlaylistSequence).on(qSequence.id.eq(qPlaylistSequence.sequenceId))
                .where(qPlaylistSequence.playlistId.eq(playlistId))
                .orderBy(qPlaylistSequence.displayOrder.asc())
                .offset(index)
                .limit(1)
                .fetchFirst()

        return Optional.ofNullable(sequence)
    }
}

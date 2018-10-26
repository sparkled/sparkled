package io.sparkled.persistence.playlist.impl.query

import io.sparkled.model.entity.PlaylistSequence
import io.sparkled.persistence.PersistenceQuery
import io.sparkled.persistence.PersistenceQuery.Companion.qPlaylistSequence
import io.sparkled.persistence.QueryFactory
import java.util.*

class GetPlaylistSequenceByUuidQuery(private val sequenceId: Int, private val uuid: UUID) : PersistenceQuery<Optional<PlaylistSequence>> {

    override fun perform(queryFactory: QueryFactory): Optional<PlaylistSequence> {
        val playlistSequence = queryFactory
                .selectFrom(qPlaylistSequence)
                .where(qPlaylistSequence.sequenceId.eq(sequenceId).and(qPlaylistSequence.uuid.eq(uuid)))
                .fetchFirst()

        return Optional.ofNullable(playlistSequence)
    }
}

package io.sparkled.persistence.playlist.impl.query

import io.sparkled.model.entity.PlaylistSequence
import io.sparkled.model.entity.QPlaylistSequence.playlistSequence
import io.sparkled.persistence.PersistenceQuery
import io.sparkled.persistence.QueryFactory
import java.util.UUID

class GetPlaylistSequenceByUuidQuery(private val sequenceId: Int, private val uuid: UUID) :
    PersistenceQuery<PlaylistSequence?> {

    override fun perform(queryFactory: QueryFactory): PlaylistSequence? {
        return queryFactory
            .selectFrom(playlistSequence)
            .where(playlistSequence.sequenceId.eq(sequenceId).and(playlistSequence.uuid.eq(uuid)))
            .fetchFirst()
    }
}

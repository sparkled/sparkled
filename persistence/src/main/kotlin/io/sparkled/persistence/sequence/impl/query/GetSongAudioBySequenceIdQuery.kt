package io.sparkled.persistence.sequence.impl.query

import io.sparkled.model.entity.SongAudio
import io.sparkled.persistence.PersistenceQuery
import io.sparkled.persistence.PersistenceQuery.Companion.qSequence
import io.sparkled.persistence.PersistenceQuery.Companion.qSongAudio
import io.sparkled.persistence.QueryFactory
import java.util.Optional

class GetSongAudioBySequenceIdQuery(private val sequenceId: Int) : PersistenceQuery<Optional<SongAudio>> {

    override fun perform(queryFactory: QueryFactory): Optional<SongAudio> {
        val songAudio = queryFactory
                .select(qSongAudio)
                .from(qSequence)
                .innerJoin(qSongAudio).on(qSequence.songId.eq(qSongAudio.songId))
                .where(qSequence.id.eq(sequenceId))
                .fetchFirst()

        return Optional.ofNullable(songAudio)
    }
}

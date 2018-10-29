package io.sparkled.persistence.sequence.impl.query

import io.sparkled.model.entity.QSequence.sequence
import io.sparkled.model.entity.QSongAudio.songAudio
import io.sparkled.model.entity.SongAudio
import io.sparkled.persistence.PersistenceQuery
import io.sparkled.persistence.QueryFactory
import java.util.Optional

class GetSongAudioBySequenceIdQuery(private val sequenceId: Int) : PersistenceQuery<Optional<SongAudio>> {

    override fun perform(queryFactory: QueryFactory): Optional<SongAudio> {
        val songAudio = queryFactory
            .select(songAudio)
            .from(sequence)
            .innerJoin(songAudio).on(sequence.songId.eq(songAudio.songId))
            .where(sequence.id.eq(sequenceId))
            .fetchFirst()

        return Optional.ofNullable(songAudio)
    }
}

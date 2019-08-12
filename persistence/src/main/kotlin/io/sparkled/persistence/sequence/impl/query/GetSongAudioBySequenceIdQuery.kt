package io.sparkled.persistence.sequence.impl.query

import io.sparkled.model.entity.QSequence.Companion.sequence
import io.sparkled.model.entity.QSongAudio.Companion.songAudio
import io.sparkled.model.entity.SongAudio
import io.sparkled.persistence.PersistenceQuery
import io.sparkled.persistence.QueryFactory

class GetSongAudioBySequenceIdQuery(private val sequenceId: Int) : PersistenceQuery<SongAudio?> {

    override fun perform(queryFactory: QueryFactory): SongAudio? {
        return queryFactory
            .select<SongAudio?>(songAudio)
            .from(sequence)
            .innerJoin<SongAudio?>(songAudio).on(sequence.songId.eq(songAudio.songId))
            .where(sequence.id.eq(sequenceId))
            .fetchFirst()
    }
}

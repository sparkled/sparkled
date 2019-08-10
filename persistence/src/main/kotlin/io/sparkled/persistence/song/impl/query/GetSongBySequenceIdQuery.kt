package io.sparkled.persistence.song.impl.query

import io.sparkled.model.entity.QSequence.Companion.sequence
import io.sparkled.model.entity.QSong.Companion.song
import io.sparkled.model.entity.Song
import io.sparkled.persistence.PersistenceQuery
import io.sparkled.persistence.QueryFactory

class GetSongBySequenceIdQuery(private val sequenceId: Int) : PersistenceQuery<Song?> {

    override fun perform(queryFactory: QueryFactory): Song? {
        return queryFactory
            .select(song)
            .from(sequence)
            .innerJoin<Song?>(song).on(sequence.songId.eq(song.id))
            .where(sequence.id.eq(sequenceId))
            .fetchFirst()
    }
}
